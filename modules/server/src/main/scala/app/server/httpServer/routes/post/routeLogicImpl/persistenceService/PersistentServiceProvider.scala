package app.server.httpServer.routes.post.routeLogicImpl.persistenceService

import akka.actor.ActorRef
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.logic.PersistentActorImpl
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.{
  ElementaryPersistenceOperation,
  EPOPExecutor
}
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.PersistentActorWhisperer
import app.shared.entity.entityValue.EntityValue

import scala.concurrent.{ExecutionContextExecutor, Future}

/**
  *
  * One layer of "abstraction" ... maybe this can be factored out.
  * Or maybe this will be good for OCC. We keep this here for now,
  * but at this point it seems that it does not do anything at all.
  *
  * The only thing it does is that it provides a Facade to a
  * PersistentActorWhisperer() but it is likely that this layer
  * is not needed. But let's wait until implementing OCC and see
  * if we still need it.
  *
  * @param context
  */
private[routes] case class PersistentServiceProvider(
)(
  implicit
  context: ExecutionContextExecutor) {

  implicit val paw = PersistentActorWhisperer()

  def executePO[
    V  <: EntityValue[V],
    OP <: ElementaryPersistenceOperation[ V ] ]
  ( par        : OP#Par )
  (
    implicit ex: EPOPExecutor[V, OP]
  ): Future[OP#Res] = {
    ex.execute(par)
  }

  // todo-later this is where we put the OCC - maybe

}
