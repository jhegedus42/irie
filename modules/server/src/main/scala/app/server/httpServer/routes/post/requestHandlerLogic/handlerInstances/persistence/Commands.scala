package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence

import app.shared.entity.entityValue.EntityValue

trait Parameter[C <: Command]

sealed trait Command {
  type Par <: Parameter[this.type]
}

trait Get[V <: EntityValue[V]] extends Command {
  override type Par = EntityParameter[V, Get[V]]
}

trait Insert extends Command

trait Update extends Command
