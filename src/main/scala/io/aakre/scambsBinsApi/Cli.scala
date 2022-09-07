package io.aakre.scambsBinsApi

import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.client.Client
import org.http4s.client.blaze.BlazeClientBuilder

import scala.concurrent.ExecutionContext.global

/**
 * simple command line client that reports which bins are being collected tomorrow
 */
object Cli extends IOApp {

  // TODO Y U Hardcoded?
  val scambsIcalUrl = "https://servicelayer3c.azure-api.net/wastecalendar/calendar/ical/10008078943"

  private def prog(client: Client[IO]): IO[String] = for {
    rawIcal <- Network.readFromUrl(scambsIcalUrl, client)
    parsed <- IO(ICalParsers.parse(ICalParsers.iCalParser, rawIcal))
    prepared <- IO(Collection.joinAndSort(parsed.get))
    text <- IO.pure(humanReadable(prepared.head))
  } yield text.getOrElse("")

  def humanReadable(c: Collection): Option[String] = {
    def binToColour(b: Bin): String = b match {
      case BlackBin => "Black"
      case BlueBin => "Blue"
      case GreenBin => "Green"
    }

    def binString: String = c match {
      case c if c.bins.length == 1 => binToColour(c.bins.head)
      case _ => c.bins.map(binToColour).mkString(" and ")
    }

    if (c.date.isTomorrow) Some(s"The ${binString} bin will be collected tomorrow")
    else None
  }

  def run(args: List[String]): IO[ExitCode] =
    BlazeClientBuilder[IO](global).resource.use { client =>
      for {
        text <- prog(client)
        _ <- IO(println(text))
      } yield ExitCode.Success
    }
}