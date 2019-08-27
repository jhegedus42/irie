package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence

import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue



trait EntityParameter[
  V <: EntityValue[V],
  C <: Command ]
    extends Parameter[C]

trait Parameter[C<:Command]

sealed trait Command{
  type Par <:Parameter[this.type]
}

trait Get[V<:EntityValue[V]] extends Command{
  override type Par = EntityParameter[V,Get[V]]
}

trait Insert extends Command
trait Update extends Command

sealed trait PersistenceError[C<:Command]

sealed trait Result[C<:Command]{
//  def getResult : Either[PersistenceError[C]]
}