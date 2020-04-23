val CatsVersion = "2.0.0"
val Http4sVersion = "0.20.10"
val CirceVersion = "0.11.0"
val ScalaTestVersion = "3.0.5"
val ParserCombinatorVersion = "1.1.1"

val ZIOVersion = "1.0.0-RC17"
val ZIOCatsInteropVersion = "2.0.0.0-RC10"

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / organization     := "io.aakre"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organizationName := "Erlend Aakre"

resolvers += Resolver.sonatypeRepo("snapshots")

lazy val root = (project in file("."))
  .settings(
    name := "scambs-bins-api",
    libraryDependencies ++= Seq(
      "org.typelevel"          %% "cats-effect"                        % CatsVersion,
      "org.scala-lang.modules" %% "scala-parser-combinators"           % ParserCombinatorVersion,
      "org.scalatest"          %% "scalatest"                          % ScalaTestVersion
    ) ++ Seq(
      "org.http4s"             %% "http4s-blaze-server",
      "org.http4s"             %% "http4s-blaze-client",
      "org.http4s"             %% "http4s-dsl"
    ).map(_ % Http4sVersion)
    ++ Seq(
      "io.circe"               %% "circe-core",
      "io.circe"               %% "circe-generic",
      "io.circe"               %% "circe-parser"
    ).map(_ % CirceVersion)
    ++ Seq(
      "dev.zio" %% "zio",
      "dev.zio" %% "zio-streams"
    ).map(_ % ZIOVersion)
   ++ Seq(
      "dev.zio" %% "zio-test",
      "dev.zio" %% "zio-test-sbt"
    ).map(_ % ZIOVersion % "test")
    ++ Seq (
      "dev.zio" %% "zio-interop-cats" % ZIOCatsInteropVersion
    )
  )

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Ypartial-unification",
)