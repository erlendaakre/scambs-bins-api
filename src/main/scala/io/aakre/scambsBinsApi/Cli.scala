package io.aakre.scambsBinsApi

import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.client.Client
import org.http4s.client.blaze.BlazeClientBuilder

import scala.concurrent.ExecutionContext.global

/**
 * simple command line client that reports which bins are being collected tomorrow
 */
object Cli extends IOApp {
  val scambsIcalUrl = "https://servicelayer3c.azure-api.net/wastecalendar/calendar/ical/10008078943"

  def x =  {
    BlazeClientBuilder[IO](global).resource.use { client =>
      prog(client)
    }
  }

  def prog(client: Client[IO]): IO[String] = for {
    rawIcal <- Network.readFromUrl(scambsIcalUrl, client)
  } yield rawIcal

  def run(args: List[String]): IO[ExitCode] = {
    IO(ExitCode.Success)
  }
}
