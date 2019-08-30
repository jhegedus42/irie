package app.server.httpServer.routes.post.routeLogicImpl.persistenceService

import akka.actor.ActorRef
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.crudOps.persistentActor.logic.PersistentActorImp
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.{PersOpExecutor, PersistenceOperation}

import scala.concurrent.{ExecutionContextExecutor, Future}

private[routes] case class PersistenceServiceFacade(
    context: ExecutionContextExecutor
) {

  val actor: ActorRef = PersistentActorImp.getActor(
    "the_one_and_only_parsistent_actor"
  )

  implicit val context_as_implicit = context

  def operationExecutor[OP <: PersistenceOperation](
      par:       OP#Par
  )(implicit ex: PersOpExecutor[OP]): Future[OP#Res] = { ex.execute(par) }

}





