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

import scala.concurrent.ExecutionContext.global

/**
 * Simple http4s web server rendering the bins collection calendar as json
 */
object Server extends IOApp {

  val scambsIcalUrl = "https://servicelayer3c.azure-api.net/wastecalendar/calendar/ical/10008078943"

  type Action = Kleisli[IO, Request[IO], Response[IO]]
  implicit val encodeDate: Encoder[Bin] = (a: Bin) => Encoder.encodeString(a.toString)

  def binProg(client: Client[IO]): IO[String] = for {
    rawIcal <- Network.readFromUrl(scambsIcalUrl, client)
    parsed <- IO(ICalParsers.parse(ICalParsers.iCalParser, rawIcal))
    prepared <- IO(Collection.joinAndSort(parsed.get))
  } yield prepared.asJson.spaces2

  val binService: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "bins" =>
      BlazeClientBuilder[IO](global).resource.use { client =>
        Ok(binProg(client))
      }
  }

  val binsApp: Action = Router("/" -> binService).orNotFound

  def run(args: List[String]): IO[ExitCode] = {
    BlazeServerBuilder[IO](global)
      .bindHttp(9056, "localhost")
      .withHttpApp(binsApp)
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)
  }
}
