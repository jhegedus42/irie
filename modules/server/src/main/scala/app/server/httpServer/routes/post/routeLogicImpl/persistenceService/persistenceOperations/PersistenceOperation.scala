package app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations

import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.PersistenceOperation.{
  OperationResult,
  OperatonParameter
}

trait PersistenceOperation {
  type Par <: OperatonParameter
  type Res <: OperationResult
}

object PersistenceOperation {

  trait OperatonParameter
  trait OperationResult
  case class OperationError[C <: PersistenceOperation](val description: String)

}
