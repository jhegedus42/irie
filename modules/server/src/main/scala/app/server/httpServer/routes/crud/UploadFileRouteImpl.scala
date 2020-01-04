package app.server.httpServer.routes.crud

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import app.server.httpServer.routes.fileUploading.UploadFileRoute

import scala.concurrent.ExecutionContextExecutor

case class UploadFileRouteImpl(
  implicit
  actorSystem:              ActorSystem,
  executionContextExecutor: ExecutionContextExecutor,
  actorMaterializer:        ActorMaterializer)
    extends UploadFileRoute {

  override implicit val system: ActorSystem = actorSystem

  override implicit def executor: ExecutionContextExecutor =
    executionContextExecutor

  override implicit val materializer: Materializer =
    actorMaterializer
}
