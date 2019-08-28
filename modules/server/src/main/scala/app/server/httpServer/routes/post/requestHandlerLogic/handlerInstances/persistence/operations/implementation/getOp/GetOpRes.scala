package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations.implementation.getOp

import app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations.types.results.errors.PersistenceError
import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue

case class GetOpRes[E <: Entity[E]](
  res: Either[PersistenceError[GetOpRes[E]],Entity[E]])
