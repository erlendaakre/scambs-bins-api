package io.aakre.scambsBinsApi

import org.scalatest.{FlatSpec, Matchers}

class CliSpec extends FlatSpec with Matchers {
  "command line client" should "produce human readable text from a collection" in {
    val bluegreen = Collection(Date(1985, 10, 26), List(BlueBin, GreenBin))
    val black = Collection(Date(1985, 10, 26), List(BlackBin))

    assert(Cli.humanReadable(black).contains("The Black bin will be collected tomorrow"))
    assert(Cli.humanReadable(bluegreen).contains("The Blue and Green bin will be collected tomorrow"))
  }
}
