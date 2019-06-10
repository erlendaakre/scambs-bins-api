package io.aakre.scambsBinsApi

import cats.data.{Kleisli, State}
import cats.effect._
import cats.effect.concurrent.Ref
import cats.implicits._
import org.http4s.server.blaze._
import org.http4s.implicits._
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.server.Router
import org.http4s.client.blaze._
import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._

import scala.concurrent.ExecutionContext.global
import scala.concurrent.duration._

object Server extends IOApp {

  type Action = Kleisli[IO, Request[IO], Response[IO]]

  val scambsIcalUrl = "https://refusecalendarapi.azurewebsites.net/calendar/ical/137912"

  val calendarCache: IO[Ref[IO, Download]] = Ref.of[IO,Download](Download(0, ""))

  implicit val encodeDate: Encoder[Bin] = (a: Bin) =>  Encoder.encodeString(a.toString)

  val binService: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "bins" =>
      BlazeClientBuilder[IO](global).withRequestTimeout(5.seconds).resource.use { client =>
        val getCalendarProg = for {
          currentTime <- IO(System.currentTimeMillis())
          cache <- calendarCache.flatMap(_.get)
          _ <- IO(println(s"Current Time   : $currentTime"))
          _ <- IO(println(s"cache time     : + " + cache.time))
          download <- if(cache.time + 20000 < currentTime) Network.downloadCalendarFromUrl(scambsIcalUrl, client) else IO(cache)
          _ <- IO(println(s"download time  : + " + download.time))
          _ <- calendarCache.map(_.set(download))
          parsed <- IO(ICalParsers.parse(ICalParsers.iCalParser, download.content))
          prepared <- IO( Logic.joinAndSort(parsed.get))
        } yield prepared.asJson.spaces2

        val result = getCalendarProg.unsafeRunSync()
        println("=== RESULT: " + result)

        Ok(getCalendarProg.unsafeRunSync()) // TODO handle network failure
      }
  }

  val binsApp: Action = Router("/" -> binService).orNotFound

  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(1701, "localhost")
      .withHttpApp(binsApp)
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)
}
