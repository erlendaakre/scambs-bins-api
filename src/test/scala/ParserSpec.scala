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
    parse(uidLine, "UID:2d816fda-fafc-4746-abcf-12b4411162d6@192.124.249.105").successful shouldBe true
  }

  it should "match an event timestamp" in {
    parse(timeStampLine, "DTSTAMP:20190211T122046Z").successful shouldBe true
  }

  it should "parse an event date line with valid date" in {
    val p = parse(dateLine, "DTSTART;VALUE=DATE:20190215")
    p.successful shouldBe true
    p.isEmpty shouldBe false
    p.get shouldEqual "20190215" // TODO this parser should return Date
  }

  it should "not parse an event date line with invalid date" in {
    val p = parse(dateLine, "DTSTART;VALUE=DATE:2019-02-05")
    p.successful shouldBe false
    p.isEmpty shouldBe true
  }

  it should "parse an event summary line for black bins" in {
    val p = parse(summaryLine, "SUMMARY:Black Bin Collection")
    p.successful shouldBe true
    p.isEmpty shouldBe false
    p.get shouldBe BlackBin
  }

  it should "parse an event summary line for blue bins" in {
    val p = parse(summaryLine, "SUMMARY:Blue Bin Collection")
    p.successful shouldBe true
    p.isEmpty shouldBe false
    p.get shouldBe BlueBin
  }

  it should "parse an event summary line for green bins" in {
    val p = parse(summaryLine, "SUMMARY:Green Bin Collection")
    p.successful shouldBe true
    p.isEmpty shouldBe false
    p.get shouldBe GreenBin
  }

  it should "not parse an event summary line for unknown bins" in {
    val p = parse(summaryLine, "SUMMARY:Purple Bin Collection")
    p.successful shouldBe false
    p.isEmpty shouldBe true
  }

  it should "match an event end line" in {
    parse(eventEndLine, "END:VEVENT").successful shouldBe true
  }

  it should "parse a multiline event entry" in {}

  it should "parse several multiline events" in {}

  it should "parse an entire iCal file (header+events)" in {}
}
