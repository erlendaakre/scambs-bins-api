package io.aakre.scambsBinsApi

import cats.data.Kleisli
import cats.effect._
import cats.implicits._
import org.http4s.server.blaze._
import org.http4s.implicits._
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.server.Router
import org.http4s.client.blaze._

import scala.concurrent.ExecutionContext.global

object Server extends IOApp {

  type Action = Kleisli[IO, Request[IO], Response[IO]]

  val scambsIcalUrl = "https://refusecalendarapi.azurewebsites.net/calendar/ical/137912"

  val binService: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "bins" =>
      BlazeClientBuilder[IO](global).resource.use { client =>
        val result = for {
          rawIcal <- Network.readFromUrl(scambsIcalUrl, client).unsafeRunSync()
          parsed <- "TODO finish parser, then parse ical into sensible domain and serialize to json"
        } yield rawIcal
        Ok(result)
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
