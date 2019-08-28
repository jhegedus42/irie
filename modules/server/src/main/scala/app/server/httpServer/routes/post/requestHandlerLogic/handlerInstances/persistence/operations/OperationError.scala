package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations

case class OperationError[C <: Operation](val description: String)
