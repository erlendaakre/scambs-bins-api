package io.aakre.scambsBinsApi

import org.scalatest._

class ParserSpec extends FlatSpec with Matchers with ICalParsers with TestData {

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
    p.get shouldEqual Date(2019, 2, 15)
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


  //----- Multi Event / Full Calendar Tests -----
  it should "parse a multiline event entry" in {
    val p = parse(eventParser, TestEvent1)
    p.successful shouldBe true
    p.isEmpty shouldBe false
    p.get shouldEqual Collection(Date(2019, 2, 14), Seq(GreenBin))
  }

  it should "parse several multiline events" in {
    val p = parse(eventsParser, TestEvent1 + "\n" + TestEvent2)
    p.successful shouldBe true
    p.isEmpty shouldBe false
    p.get.length shouldBe 2
    p.get should contain(Collection(Date(2019, 2, 14), Seq(GreenBin)))
    p.get should contain(Collection(Date(2019, 2, 14), Seq(BlueBin)))
  }

  it should "parse an entire calendar (header+events)" in {
    val p = parse(iCalParser, TestCalendar)
    p.successful shouldBe true
    p.isEmpty shouldBe false
    p.get.length shouldBe 6
    p.get should contain(Collection(Date(2019, 2, 15), Seq(BlackBin)))
    p.get should contain(Collection(Date(2019, 3, 15), Seq(BlackBin)))
    p.get.count(_.bins.contains(GreenBin)) shouldBe 1
    p.get.count(_.bins.contains(BlueBin)) shouldBe 2
    p.get.count(_.bins.contains(BlackBin)) shouldBe 3
  }
}