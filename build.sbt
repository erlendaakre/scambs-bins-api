val CatsEffectVersion = "2.3.1"
val Http4sVersion = "0.21.19"
val CirceVersion = "0.11.0"
val ScalaTestVersion = "3.0.5"
val ParserCombinatorVersion = "1.1.1"

val scalaVersion = "2.12.8"
val sbtVersion = "1.4.3"
val version = "0.1.0-SNAPSHOT"
val organization = "io.aakre"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-effect" % CatsEffectVersion,
  "org.scala-lang.modules" %% "scala-parser-combinators" % ParserCombinatorVersion,
  "org.scalatest" %% "scalatest" % ScalaTestVersion
)

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-blaze-server",
  "org.http4s" %% "http4s-blaze-client",
  "org.http4s" %% "http4s-dsl"
).map(_ % Http4sVersion)

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % CirceVersion)

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Ypartial-unification",
)