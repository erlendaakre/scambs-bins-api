package io.aakre.scambsBinsApi

import cats.effect.IO
import org.http4s.client._

object Network {
  def downloadCalendarFromUrl(url: String, c: Client[IO]): IO[Download] = for {
    data <- c.expect[String](url)
    time <- IO(System.currentTimeMillis())
    _ <- IO(println("Downloaded calendar at " + time))
  } yield Download(time,data)
}

import scala.util.parsing.combinator._
object ICalParsers extends RegexParsers {
  def calendarStartLine: Parser[String]     = "BEGIN:VCALENDAR"
  def secondLine: Parser[String]            = "PRODID:(.+)//Waste Calendar Generator//".r
  def versionLine: Parser[String]           = "VERSION:2.0"
  def nameLine: Parser[String]              = "X-WR-CALNAME:Bins Schedule"
  def descLine: Parser[String]              = "X-WR-CALDESC:Bins Schedule"
  def timezoneLine: Parser[String]          = "X-WR-TIMEZONE:Europe/London"
  def headerParser: Parser[Unit]            = calendarStartLine ~ secondLine ~ versionLine ~
                                              nameLine ~ descLine ~ timezoneLine                ^^ { _ => ()  }

  def eventStartLine: Parser[String]        = "BEGIN:VEVENT"
  def uidLine : Parser[String]              = "UID:(.+)".r
  def timeStampLine : Parser[String]        = "DTSTAMP:(.+)".r
  def dateLine : Parser[Date]               = "DTSTART;VALUE=DATE:" ~> year ~ month ~ day ^^
                                              { case y ~ m ~ d => Date(y,m,d) }
  def year : Parser[Int]                    = "[0-9]{4}".r                                      ^^ { _.toInt }
  def month : Parser[Int]                   = "[0|1][0-9]".r                                    ^^ { _.toInt }
  def day : Parser[Int]                     = "[0-3][0-9]".r                                    ^^ { _.toInt }
  def summaryLine : Parser[Bin]             = "SUMMARY:" ~> binType                             ^^ { parseSummary }
  def binType: Parser[String]               = "(Black|Blue|Green)".r <~ "Bin Collection"
  def eventEndLine: Parser[String]          = "END:VEVENT"
  def eventParser: Parser[Collection]       = eventStartLine ~ uidLine ~ timeStampLine ~
                                              dateLine ~ summaryLine ~ eventEndLine             ^^
                                              { case _ ~ _ ~ _ ~ d ~ b ~ _ => Collection(d,Seq(b)) }

  def eventsParser: Parser[Seq[Collection]] = eventParser.*
  def iCalParser: Parser[Seq[Collection]]   = headerParser ~> eventsParser <~ calendarEndLine

  def calendarEndLine: Parser[String]       = "END:VCALENDAR"

  private def parseSummary(s: String): Bin = s match {
    case "Blue" => BlueBin
    case "Green" => GreenBin
    case "Black" => BlackBin
  }
}

object Logic { // Rename me
  val joinAndSort: Seq[Collection] => Seq[Collection] = joinBinsOnDate _ andThen sortByDate

  private def joinBinsOnDate(cs: Seq[Collection]) =
    cs.groupBy(_.date).map(t => Collection(t._1, t._2.flatMap(_.bins))).toSeq

  private def sortByDate(cs: Seq[Collection]) = cs.sortBy(c => c.date)

}