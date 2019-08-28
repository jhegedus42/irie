package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations

trait OpExecutor[OP<:Operation]{
  def execute(par:OP#Par):OP#Res
}
