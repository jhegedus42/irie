package app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations

import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.crudOps.Get
import app.shared.entity.entityValue.EntityValue

import scala.concurrent.Future

trait PersistenceOperationExecutorTypeClass[OP<:PersistenceOperation]{
  def execute(par:OP#Par):Future[OP#Res]
}

object PersistenceOperationExecutorTypeClass {

  implicit def getOperationInstance[V<:EntityValue[V]]= Get.
    GetPersistenceOperationExecutorTypeClassImpl[V]()

  // ElKelKaposzTasTalanItHatatLanSagosKodAsOitokErt

}