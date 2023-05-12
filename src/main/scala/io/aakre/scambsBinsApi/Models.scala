package io.aakre.scambsBinsApi

import java.time.LocalDate

sealed trait Bin
case object BlueBin extends Bin
case object GreenBin extends Bin
case object BlackBin extends Bin

final case class Date(year: Int, month: Int, day: Int) extends Ordered[Date] {
  override def compare(that: Date): Int = {
    Ordering.Tuple3(Ordering.Int, Ordering.Int, Ordering.Int).compare((year, month, day), (that.year, that.month, that.day))
  }

  def isTomorrow: Boolean = isTomorrow(LocalDate.now())
  def isTomorrow(now: LocalDate): Boolean = now.plusDays(1).isEqual(LocalDate.of(year, month, day))
  def isInPast: Boolean = isInPast(LocalDate.now())
  def isInPast(now: LocalDate): Boolean = LocalDate.of(year,month,day).isBefore(now)
}

final case class Collection(date: Date, bins: List[Bin]) { self =>
  def ++(that: Collection): Collection = Collection(date, self.bins ++ that.bins)
  def +(bin: Bin): Collection = this.copy(bins = bin :: bins)
}

object Collection {
  def joinAndSort: List[Collection] => List[Collection] = joinBinsOnDate andThen sortByDate andThen dropCollectionsInPast

  val joinBinsOnDate = (cs: List[Collection]) =>
    cs.groupBy(_.date).map { case (date, bins) => Collection(date, bins.flatMap(_.bins)) }.toList

  val sortByDate = (cs: List[Collection]) => cs.sortBy(c => c.date)

  val dropCollectionsInPast = (cs: List[Collection]) => cs.filterNot(c => c.date.isInPast)
}