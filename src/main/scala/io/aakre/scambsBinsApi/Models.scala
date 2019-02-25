package io.aakre.scambsBinsApi

sealed trait Bin
case object BlueBin extends Bin
case object GreenBin extends Bin
case object BlackBin extends Bin

case class Date(year: Int, month: Int, day: Int) extends Ordered[Date] {
  override def compare(that: Date): Int = {
    Ordering.Tuple3(Ordering.Int, Ordering.Int, Ordering.Int).compare((year, month, day), (that.year, that.month, that.day))
  }
}

case class Collection(date: Date, bins: Seq[Bin])

case class Download(time: Long, content: String)