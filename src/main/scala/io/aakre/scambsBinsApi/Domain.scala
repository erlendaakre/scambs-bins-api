package io.aakre.scambsBinsApi

import cats.effect.IO
import org.http4s.client._

object Network {
  def readFromUrl(url: String, c: Client[IO]): IO[String] = c.expect[String](url)
}

import scala.util.parsing.combinator._
trait ICalParsers extends RegexParsers {
  def calendarStartLine: Parser[String]   = "BEGIN:VCALENDAR"
  def secondLine: Parser[String]          = "PRODID:(.+)//Waste Calendar Generator//".r
  def versionLine: Parser[String]         = "VERSION:2.0"
  def nameLine: Parser[String]            = "X-WR-CALNAME:Bins Schedule"
  def descLine: Parser[String]            = "X-WR-CALDESC:Bins Schedule"
  def timezoneLine: Parser[String]        = "X-WR-TIMEZONE:Europe/London"
  def headerParser: Parser[Unit]          = calendarStartLine ~ secondLine ~ versionLine ~
                                            nameLine ~ descLine ~ timezoneLine                     ^^ { _ => ()  }

  def eventStartLine: Parser[String]      = "BEGIN:VEVENT"
  def uidLine : Parser[String]            = "UID:(.+)".r
  def timeStampLine : Parser[String]      = "DTSTAMP:(.+)".r
  def dateLine : Parser[String]           = "DTSTART;VALUE=DATE:" ~> "[0-9]{8}".r
  def summaryLine : Parser[Bin]           = "SUMMARY:" ~> binType                                  ^^ { parseSummary }
  def binType: Parser[String]             = "(Black|Blue|Green)".r <~ "Bin Collection"
  def eventEndLine: Parser[String]        = "END:VEVENT"
  def eventParser: Parser[Collection]     = "TODO:WRITE:ME"                                        ^^ { parseEvent }

  def calendarEndLine: Parser[String]     = "END:VCALENDAR"


  private def parseEvent(s: String) : Collection = {
    // TODO
    Collection(Date(2019,2,20), Seq(BlueBin))
  }

  private def parseDate(s: String) : Date = {
    // TODO make parser for this?
    Date(2019,2,20)
  }

  private def parseSummary(s: String) : Bin = {
    s match {
      case "Blue" => BlueBin
      case "Green" => GreenBin
      case "Black" => BlackBin
    }
  }
}