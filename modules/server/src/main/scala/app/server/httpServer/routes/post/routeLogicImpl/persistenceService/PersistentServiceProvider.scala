package app.server.httpServer.routes.post.routeLogicImpl.persistenceService

import akka.actor.ActorRef
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.logic.PersistentActorImp
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.{
  PersistenceOperationExecutorTypeClass,
  PersistenceOperation
}

import scala.concurrent.{ExecutionContextExecutor, Future}

private[routes] case class PersistentServiceProvider(
    context: ExecutionContextExecutor
) {

  implicit val context_as_implicit = context

  def executePersistenceOperation[OP <: PersistenceOperation](
      par:       OP#Par
  )(implicit ex: PersistenceOperationExecutorTypeClass[OP]): Future[OP#Res] = { ex.execute(par) }

}
