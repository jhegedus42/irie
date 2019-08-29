package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations

import app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations.crud.Get
import app.shared.entity.entityValue.EntityValue

trait OpExecutor[OP<:Operation]{
  def execute(par:OP#Par):OP#Res
}

object OpExecutor {

  implicit def get[V<:EntityValue[V]]= Get.GetOpExecutorImpl[V]()
}

