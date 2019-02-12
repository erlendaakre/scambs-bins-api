package io.aakre.scambsBinsApi

import cats.effect.IO
import org.http4s.client._

object Network {
  def readFromUrl(url: String, c: Client[IO]): IO[String] = c.expect[String](url)
}

import scala.util.parsing.combinator._
trait ICalParsers extends RegexParsers {
  def calendarStartLine: Parser[String] = """BEGIN:VCALENDAR"""
  def secondLine: Parser[String] = """PRODID:(.+)//Waste Calendar Generator//""".r
  def versionLine: Parser[String] = """VERSION:2.0"""
  def nameLine: Parser[String] = """X-WR-CALNAME:Bins Schedule"""
  def descLine: Parser[String] = """X-WR-CALDESC:Bins Schedule"""
  def timezoneLine: Parser[String] = """X-WR-TIMEZONE:Europe/London"""
  def headerParser: Parser[String] = calendarStartLine ~ secondLine ~ versionLine ~
                                     nameLine ~ descLine ~ timezoneLine                      ^^ { _ => ""  }

  def eventStartLine: Parser[String] = """BEGIN:VEVENT"""
  def UIDLine : Parser[String] = """UID:(.+)""".r
  def TimeStampLine : Parser[String] = """DTSTAMP:(.+)""".r
  def DateLine : Parser[String] = """DTSTART:(.+)""".r                                       ^^ { _ => ""  }
  def SummaryLine : Parser[String] = """SUMMARY:(.+)""".r                                    ^^ { _ => ""  }
  def eventEndLine: Parser[String] = """END:VEVENT"""
  def eventParser: Parser[Collection] = """TODO:WRITE:ME"""                          ^^ { n =>  Collection(null, null) }

  def calendarEndLine: Parser[String] = """END:VCALENDAR"""

  // TODO make composed event parser that repeats until lastLine

  /*
  https://github.com/scala/scala-parser-combinators
   */
}

/*
BEGIN:VEVENT
UID:2d816fda-fafc-4746-abcf-12b4411162d6@192.124.249.105
DTSTAMP:20190211T122046Z

SUMMARY:Black Bin Collection
END:VEVENT
 */