val CatsVersion = "1.2.0"
val Http4sVersion = "0.20.0-SNAPSHOT"
val CirceVersion = "0.11.0"
val ScalaTestVersion = "3.0.5"
val ParserCombinatorVersion = "1.1.1"

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "io.aakre"
ThisBuild / organizationName := "Erlend Aakre"


resolvers += Resolver.sonatypeRepo("snapshots")

lazy val root = (project in file("."))
  .settings(
    name := "scambs-bins-api",
    libraryDependencies ++= Seq(
      "org.typelevel"          %% "cats-effect"                        % CatsVersion,
      "org.scala-lang.modules" %% "scala-parser-combinators"           % ParserCombinatorVersion,
      "org.scalatest"          %% "scalatest"                          % ScalaTestVersion
    ),
    libraryDependencies ++= Seq(
      "org.http4s"             %% "http4s-blaze-server",
      "org.http4s"             %% "http4s-blaze-client",
      "org.http4s"             %% "http4s-dsl"
    ).map(_ % Http4sVersion),
    libraryDependencies ++= Seq(
      "io.circe"               %% "circe-core",
      "io.circe"               %% "circe-generic",
      "io.circe"               %% "circe-parser"
    ).map(_ % CirceVersion)
  )

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Ypartial-unification",
)