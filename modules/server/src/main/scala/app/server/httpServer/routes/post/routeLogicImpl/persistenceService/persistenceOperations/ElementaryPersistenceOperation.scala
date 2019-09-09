package app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations

import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.ElementaryPersistenceOperation.{
  OperationResult,
  OperatonParameter
}
import app.shared.entity.entityValue.EntityValue

// todo-later : parametrize this typeclass by EntityValue
//  that will allow to merge some type classes and simplify the
//  code, in general
/**
  *
  * Entity level persistence operation.
  * Should involve operation only on one
  * single Entity.
  *
  */
trait ElementaryPersistenceOperation[V <: EntityValue[V]] {
  type Par <: OperatonParameter
  type Res <: OperationResult
}

object ElementaryPersistenceOperation {

  trait OperatonParameter
  trait OperationResult

  case class OperationError(
      val description: String
  )

}
