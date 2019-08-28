package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations





trait Operation{
  type OP<:Operation
  type Par<:OperatonParameter
  type Res<:OperationResult
}




