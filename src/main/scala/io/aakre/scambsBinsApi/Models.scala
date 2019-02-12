package io.aakre.scambsBinsApi

sealed trait Bin
case object BlueBin extends Bin
case object GreenBin extends Bin
case object BlackBin extends Bin

case class Date(year: Int, Month: Int, day: Int)

case class Collection(date: Date, bins: Seq[Bin])