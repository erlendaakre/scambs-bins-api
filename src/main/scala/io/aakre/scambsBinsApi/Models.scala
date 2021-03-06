package io.aakre.scambsBinsApi

sealed trait Bin
case object BlueBin extends Bin
case object GreenBin extends Bin
case object BlackBin extends Bin

final case class Date(year: Int, month: Int, day: Int) extends Ordered[Date] {
  override def compare(that: Date): Int = {
    Ordering.Tuple3(Ordering.Int, Ordering.Int, Ordering.Int).compare((year, month, day), (that.year, that.month, that.day))
  }
}

final case class Collection(date: Date, bins: List[Bin]) { self =>
  /**
   * Adds the bins of the given collection to this one
   *
   * @param that the second collection to copy the bins from
   * @return a new collection with combined bins
   */
  def ++(that: Collection): Collection = {
    Collection(date, self.bins ++ that.bins)
  }

  def +(bin: Bin): Collection = this.copy(bins = bin :: bins)
}

object Collection {
  def joinAndSort: List[Collection] => List[Collection] = joinBinsOnDate _ andThen sortByDate

  private def joinBinsOnDate(cs: List[Collection]) =
    cs.groupBy(_.date).map { case (date, bins) => Collection(date, bins.flatMap(_.bins)) }.toList

  private def sortByDate(cs: List[Collection]) = cs.sortBy(c => c.date)
}