package app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations

import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.crudOps.Get
import app.shared.entity.entityValue.EntityValue

import scala.concurrent.Future

trait PersOpExecutor[OP<:PersistenceOperation]{
  def execute(par:OP#Par):Future[OP#Res]
}

object PersOpExecutor {

  implicit def getOperationInstance[V<:EntityValue[V]]= Get.GetPersOpExecutorImpl[V]()
}