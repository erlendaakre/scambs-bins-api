package io.aakre.scambsBinsApi

import cats.data.Kleisli
import cats.effect._
import org.http4s.server.blaze._
import org.http4s.implicits._
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.server.Router
import org.http4s.client.blaze._
import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.client.Client

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.global

object Server extends IOApp {

  type Action = Kleisli[IO, Request[IO], Response[IO]]

  val scambsIcalUrl = "https://servicelayer3c.azure-api.net/wastecalendar/calendar/ical/10008078943"

  implicit val encodeDate: Encoder[Bin] = (a: Bin) =>  Encoder.encodeString(a.toString)

  val binService: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "bins" =>
      BlazeClientBuilder[IO](global).resource.use { client =>
        Ok(binProg(client).unsafeRunSync())
      }
  }

  val binsApp: Action = Router("/" -> binService).orNotFound

  def binProg(client: Client[IO]): IO[String] = for {
    rawIcal <- Network.readFromUrl(scambsIcalUrl, client)
    parsed <- IO(ICalParsers.parse(ICalParsers.iCalParser, rawIcal))
    prepared <- IO( Logic.joinAndSort(parsed.get))
  } yield prepared.asJson.spaces2

  def run(args: List[String]): IO[ExitCode] = {
   // val ec: ExecutionContext = ExecutionContext.global

    BlazeServerBuilder[IO]
      .bindHttp(1701, "localhost")
      .withHttpApp(binsApp)
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)
  }
}
