package io.aakre.scambsBinsApi

trait TestData {
  val TestHeader: String =
    """BEGIN:VCALENDAR
      |PRODID:-//192.124.249.105//Waste Calendar Generator//
      |VERSION:2.0
      |X-WR-CALNAME:Bins Schedule
      |X-WR-CALDESC:Bins Schedule
      |X-WR-TIMEZONE:Europe/London""".stripMargin

  val TestEvent1: String =
    """BEGIN:VEVENT
      |UID:2d816fda-fafc-4746-abcf-12b4411162d6@192.124.249.105
      |DTSTAMP:20190211T122046Z
      |DTSTART;VALUE=DATE:20190214
      |SUMMARY:Green Bin Collection
      |END:VEVENT""".stripMargin

  val TestEvent2: String =
    """BEGIN:VEVENT
      |UID:2d816fda-fafc-4746-abcf-12b4411162d6@192.124.249.105
      |DTSTAMP:20190211T122046Z
      |DTSTART;VALUE=DATE:20190214
      |SUMMARY:Blue Bin Collection
      |END:VEVENT""".stripMargin

  val TestCalendar: String =
    """BEGIN:VCALENDAR
      |PRODID:-//192.124.249.105//Waste Calendar Generator//
      |VERSION:2.0
      |X-WR-CALNAME:Bins Schedule
      |X-WR-CALDESC:Bins Schedule
      |X-WR-TIMEZONE:Europe/London
      |BEGIN:VEVENT
      |UID:2d816fda-fafc-4746-abcf-12b4411162d6@192.124.249.105
      |DTSTAMP:20190211T122046Z
      |DTSTART;VALUE=DATE:20190215
      |SUMMARY:Black Bin Collection
      |END:VEVENT
      |BEGIN:VEVENT
      |UID:98482b99-3068-407f-87a7-cc2dee424ba6@192.124.249.105
      |DTSTAMP:20190211T122046Z
      |DTSTART;VALUE=DATE:20190222
      |SUMMARY:Blue Bin Collection
      |END:VEVENT
      |BEGIN:VEVENT
      |UID:00cebee4-1742-49bf-8d72-adf0e9714803@192.124.249.105
      |DTSTAMP:20190211T122046Z
      |DTSTART;VALUE=DATE:20190301
      |SUMMARY:Black Bin Collection
      |END:VEVENT
      |BEGIN:VEVENT
      |UID:00b249ce-9656-47ff-9a51-9c858f476c8a@192.124.249.105
      |DTSTAMP:20190211T122046Z
      |DTSTART;VALUE=DATE:20190308
      |SUMMARY:Green Bin Collection
      |END:VEVENT
      |BEGIN:VEVENT
      |UID:a92654f3-6101-4884-84ec-f7ab539ca616@192.124.249.105
      |DTSTAMP:20190211T122046Z
      |DTSTART;VALUE=DATE:20190308
      |SUMMARY:Blue Bin Collection
      |END:VEVENT
      |BEGIN:VEVENT
      |UID:986ed0ef-2d77-47f9-8e7b-c5555bb2ba37@192.124.249.105
      |DTSTAMP:20190211T122046Z
      |DTSTART;VALUE=DATE:20190315
      |SUMMARY:Black Bin Collection
      |END:VEVENT
      |END:VCALENDAR""".stripMargin

}
