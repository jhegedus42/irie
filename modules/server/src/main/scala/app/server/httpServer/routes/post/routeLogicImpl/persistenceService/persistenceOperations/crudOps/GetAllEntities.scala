package app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.crudOps

import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.occ.OCCVersion
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.{
  EPOPExecutor,
  ElementaryPersistenceOperation
}
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.PersistentActorWhisperer
import app.shared.entity.entityValue.EntityValue

import scala.concurrent.Future

object GetAllEntities {

  trait GetAllEntitiesEPOP[V <: EntityValue[V]]
      extends ElementaryPersistenceOperation[V]

  case class ExecutorImpl[V <: EntityValue[V]]()
      extends EPOPExecutor[V, GetAllEntitiesEPOP[V]] {
    override def execute(
      par: GetAllEntitiesEPOP[V]#Par
    )(
      implicit typeSafeAccessToPersistentActorProvider: PersistentActorWhisperer
    ): Future[GetAllEntitiesEPOP[V]#Res] = {

      ???
    }

    override def getOCCVersion: OCCVersion = ???
  }

}
