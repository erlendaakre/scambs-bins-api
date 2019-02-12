package io.aakre.scambsBinsApi

import org.scalatest._

class ParserSpec extends FlatSpec with Matchers with ICalParsers {

  val TestHeader: String =
    """BEGIN:VCALENDAR
      |PRODID:-//192.124.249.105//Waste Calendar Generator//
      |VERSION:2.0
      |X-WR-CALNAME:Bins Schedule
      |X-WR-CALDESC:Bins Schedule
      |X-WR-TIMEZONE:Europe/London""".stripMargin

  val TestEvent: String =
    """BEGIN:VEVENT
      |UID:2d816fda-fafc-4746-abcf-12b4411162d6@192.124.249.105
      |DTSTAMP:20190211T122046Z
      |DTSTART;VALUE=DATE:20190215
      |SUMMARY:Black Bin Collection
      |END:VEVENT""".stripMargin

  // ----- Header tests -----
  it should "match the first line" in {
    parse(calendarStartLine, "BEGIN:VCALENDAR").successful shouldBe true
  }

  it should "match the second line" in {
    parse(secondLine, "PRODID:-//192.124.249.105//Waste Calendar Generator//").successful shouldBe true
  }

  it should "match the name line" in {
    parse(nameLine, "X-WR-CALNAME:Bins Schedule").successful shouldBe true
  }

  it should "match the desc line" in {
    parse(descLine, "X-WR-CALDESC:Bins Schedule").successful shouldBe true
  }

  it should "match the time zone line" in {
    parse(timezoneLine, "X-WR-TIMEZONE:Europe/London").successful shouldBe true
  }

  it should "match the last line" in {
    parse(calendarEndLine, "END:VCALENDAR").successful shouldBe true
  }

  it should "match the multiline header" in {
    parse(headerParser, TestHeader).successful shouldBe true
  }


  //----- Event tests -----
  it should "match an event start line" in {
    parse(eventStartLine, "BEGIN:VEVENT").successful shouldBe true
  }

  it should "match an event UID line" in {
    parse(eventUIDLine, "UID:2d816fda-fafc-4746-abcf-12b4411162d6@192.124.249.105").successful shouldBe true
  }

  it should "match an event timestamp" in {
    parse(eventDateTimestampLine, "DTSTAMP:20190211T122046Z").successful shouldBe true
  }

  it should "match an event date line" in {
    parse(eventDateLine, "DTSTART;VALUE=DATE:20190215").successful shouldBe true


  }

  it should "parse an event summary line for black bins" in {
    val p = parse(eventSummaryLine, "SUMMARY:Black Bin Collection")
    p.successful shouldBe true
    println("=========BLACK BIN TEST ==================") // TODO delete
    println(p)
  }

  it should "parse an event summary line for blue bins" in {
    parse(eventSummaryLine, "SUMMARY:Blue Bin Collection").successful shouldBe true
  }

  it should "parse an event summary line for green bins" in {
    parse(eventSummaryLine, "SUMMARY:Green Bin Collection").successful shouldBe true
  }

  it should "match an event end line" in {
    parse(eventEndLine, "END:VEVENT").successful shouldBe true
  }

  it should "match a multiline event entry" in {}

  it should "match several multiline events in" {}



  """
    |
    |
    |
    |
    |"""
}
