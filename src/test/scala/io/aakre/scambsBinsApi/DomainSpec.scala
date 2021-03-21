package io.aakre.scambsBinsApi

import org.scalatest.{FlatSpec, Matchers}

class DomainSpec extends FlatSpec with Matchers {
  "adding a bin to a collection" should "produce a new collection with the additional bin" in {
    val collection = Collection(Date(1985, 10, 26), List(BlueBin))
    val res = collection + GreenBin
    assert(res.bins.size == 2)
    assert(res.bins.contains(BlueBin))
    assert(res.bins.contains(GreenBin))
  }
}
