package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations

import app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations.Operation.{OperationResult, OperatonParameter}


trait Operation{
  type Par<:OperatonParameter
  type Res<:OperationResult
}

object Operation{

  trait OperatonParameter
  trait OperationResult
  case class OperationError[C <: Operation](val description: String)

}



