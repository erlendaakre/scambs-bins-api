package io.aakre.scambsBinsApi

import org.scalatest.{FlatSpec, Matchers}

import java.time.LocalDate

class DomainSpec extends FlatSpec with Matchers {
  "adding a bin to a collection" should "produce a new collection with the additional bin" in {
    val collection = Collection(Date(1985, 10, 26), List(BlueBin))
    val res = collection + GreenBin
    assert(res.bins.size == 2)
    assert(res.bins.contains(BlueBin))
    assert(res.bins.contains(GreenBin))
  }

  "checking if collection date is tomorrow" should "return false if tomorrow is not a collection" in {
    val collection = Collection(Date(2022, 9, 7), List(BlackBin))
    val fakeNow = LocalDate.of(2022, 9, 5)
    assert(! collection.date.isTomorrow(fakeNow))
  }
  "checking if collection date is tomorrow" should "return true for sequential dates" in {
    val collection = Collection(Date(2022, 9, 7), List(BlackBin))
    val fakeNow = LocalDate.of(2022, 9, 6)
    assert(collection.date.isTomorrow(fakeNow))
  }
  "checking if collection date is tomorrow" should "return true if tomorrow is first day of month" in {
    val collection = Collection(Date(2022, 10,1), List(BlueBin, GreenBin))
    val fakeNow = LocalDate.of(2022, 9, 30)
    assert(collection.date.isTomorrow(fakeNow))
  }
  "checking if collection date is tomorrow" should "return true if today is NYE" in {
    val collection = Collection(Date(2024, 1,1), List(BlueBin, GreenBin))
    val fakeNow = LocalDate.of(2023, 12, 31)
    assert(collection.date.isTomorrow(fakeNow))
  }
}
