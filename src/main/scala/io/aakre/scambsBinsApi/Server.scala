package io.aakre.scambsBinsApi

import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.server.blaze._
import zio._
import zio.interop.catz._
import zio.interop.catz.implicits._

import scala.concurrent.ExecutionContext.global

object Server extends App {

  val server: ZIO[ZEnv, Throwable, Unit] = ZIO.runtime[ZEnv]
    .flatMap {
      implicit rts =>
        BlazeClientBuilder[Task](global).resource.use { client =>
          BlazeServerBuilder[Task]
            .bindHttp(8081, "localhost")
            .withHttpApp(Service.binService(client))
            .serve
            .compile
            .drain
        }
    }

  def run(args: List[String]) = server.fold(_ => 1, _ => 0)
}
