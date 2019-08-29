package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations

import app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations.crudOps.Get
import app.shared.entity.entityValue.EntityValue

import scala.concurrent.Future

trait OpExecutor[OP<:Operation]{
  def execute(par:OP#Par):Future[OP#Res]
}

object OpExecutor {

  implicit def getOperationInstance[V<:EntityValue[V]]= Get.GetOpExecutorImpl[V]()
}

