package app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations

import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.crudOps.Get
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.occ.OCCVersion
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.PersistentActorWhisperer
import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue
import io.circe.Decoder

import scala.concurrent.Future

trait PersistenceOperationExecutorTypeClass[OP <: PersistenceOperation] {
  def execute(par: OP#Par)(
      implicit
      typeSafeAccessToPersistentActorProvider: PersistentActorWhisperer
      // ElKelKaposzTasTalanItHatatLanSagosKodAsAitokErt
  ):                 Future[OP#Res]
  def getOCCVersion: OCCVersion
}

object PersistenceOperationExecutorTypeClass {

  def getOperationInstance[V <: EntityValue[V]](
      implicit d: Decoder[Entity[V]]
  ) =
    Get.GetPersistenceOperationExecutorTypeClassImpl[V](d)

}
