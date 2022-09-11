package io.aakre.scambsBinsApi

import org.scalatest.{FlatSpec, Matchers}

import java.time.LocalDate

class CliSpec extends FlatSpec with Matchers {
  "command line client" should "produce human readable text from a collection" in {
    val tomorrow =  LocalDate.now().plusDays(1)
    val tomorrowDate = Date(tomorrow.getYear, tomorrow.getMonthValue, tomorrow.getDayOfMonth)
    val bluegreen = Collection(tomorrowDate, List(BlueBin, GreenBin))
    val black = Collection(tomorrowDate, List(BlackBin))
    val none = Collection(Date(1985, 7, 3), List(BlueBin))

    assert(Cli.humanReadable(black).contains("The Black bin will be collected tomorrow"))
    assert(Cli.humanReadable(bluegreen).contains("The Blue and Green bin will be collected tomorrow"))
    assert(Cli.humanReadable(none).isEmpty)
  }
}
