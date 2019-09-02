package app.server.httpServer.routes.post.routeLogicImpl.persistenceService

import akka.actor.ActorRef
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.logic.PersistentActorImpl
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.{PersistenceOperation, PersistenceOperationExecutorTypeClass}
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.PersistentActorWhisperer

import scala.concurrent.{ExecutionContextExecutor, Future}

private[routes] case class PersistentServiceProvider(
    context: ExecutionContextExecutor
) {

  implicit val context_as_implicit = context
  implicit val paw=PersistentActorWhisperer()

  def executePersistenceOperation[OP <: PersistenceOperation](
      par:       OP#Par
  )(implicit ex: PersistenceOperationExecutorTypeClass[OP]): Future[OP#Res] = {
    // todo-later this is where we put the OCC
    ex.execute(par)
  }

}
