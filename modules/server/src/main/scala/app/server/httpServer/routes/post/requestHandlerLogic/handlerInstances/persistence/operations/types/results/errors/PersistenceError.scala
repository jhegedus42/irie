package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations.types.results.errors

import app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations.Operation

case class PersistenceError[C <: Operation](
  val description: String)
