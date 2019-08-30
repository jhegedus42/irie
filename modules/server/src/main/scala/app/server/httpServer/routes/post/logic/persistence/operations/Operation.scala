package app.server.httpServer.routes.post.logic.persistence.operations

import app.server.httpServer.routes.post.logic.persistence.operations.Operation.{OperationResult, OperatonParameter}


trait Operation{
  type Par<:OperatonParameter
  type Res<:OperationResult
}

object Operation{

  trait OperatonParameter
  trait OperationResult
  case class OperationError[C <: Operation](val description: String)

}



