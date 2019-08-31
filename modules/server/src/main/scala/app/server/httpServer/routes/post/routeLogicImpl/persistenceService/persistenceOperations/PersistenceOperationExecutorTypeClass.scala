package app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations

import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.crudOps.Get
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.occ.OCCVersion
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.TypeSafeAccessToPersistentActorProvider
import app.shared.entity.entityValue.EntityValue

import scala.concurrent.Future

trait PersistenceOperationExecutorTypeClass[OP <: PersistenceOperation] {
  def execute(par: OP#Par)(
      implicit
      typeSafeAccessToPersistentActorProvider: TypeSafeAccessToPersistentActorProvider
   // ElKelKaposzTasTalanItHatatLanSagosKodAsAitokErt

  ):                 Future[OP#Res]
  def getOCCVersion: OCCVersion
}

object PersistenceOperationExecutorTypeClass {

  implicit def getOperationInstance[V <: EntityValue[V]] =
    Get.GetPersistenceOperationExecutorTypeClassImpl[V]()


}
