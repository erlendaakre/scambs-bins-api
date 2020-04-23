package zhx.servers

import java.io.IOException

import io.aakre.scambsBinsApi.{Bin, Collection, ICalParsers, Logic}
import io.circe.Encoder
import io.circe.syntax._
import org.http4s.HttpRoutes
import org.http4s.blaze.http.HttpClient
import org.http4s.client.Client
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits._
import zio.Task
import zio.interop.catz._
import zio.interop.catz.implicits

import scala.concurrent.ExecutionContext.global

object Service {

  private val dsl = Http4sDsl[Task]

  import dsl._

  implicit val encodeDate: Encoder[Bin] = (a: Bin) => Encoder.encodeString(a.toString)

//  implicit val encodeCollection: Encoder[Collection] = (c: Collection) =>

  def binService(client: Client[Task]) = HttpRoutes.of[Task] {
    case GET -> Root / "bins" =>

      for {
        rawIcal <- readFromUrl(scambsIcalUrl, client)
        parsed <- Task(ICalParsers.parse(ICalParsers.iCalParser, rawIcal))
        prepared <- Task(Logic.joinAndSort(parsed.get))
//        response <- Ok(prepared.toList.asJson.spaces2) // TODO FIX ME FOR THE LOVE OF GLOB!!!!
        response <- Ok(prepared.head.bins.map(_.toString).asJson.spaces2)
      } yield response
  }.orNotFound

  val scambsIcalUrl = "https://servicelayer3c.azure-api.net/wastecalendar/calendar/ical/10008078943"


  def readFromUrl(url: String, c: Client[Task]): zio.IO[IOException, String] =
    c.expect[String](url).refineToOrDie[IOException]

}
