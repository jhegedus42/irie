package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations

import app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations.crud.Get
import app.shared.entity.entityValue.EntityValue

import scala.concurrent.Future

trait OpExecutor[OP<:Operation]{
  def execute(par:OP#Par):Future[OP#Res]
}

object OpExecutor {

  implicit def get[V<:EntityValue[V]]= Get.GetOpExecutorImpl[V]()
}

