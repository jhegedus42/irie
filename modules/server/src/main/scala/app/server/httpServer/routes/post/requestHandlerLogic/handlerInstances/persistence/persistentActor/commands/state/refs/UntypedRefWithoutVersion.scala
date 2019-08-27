package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.persistentActor.commands.state.refs

import app.shared.entity.asString.EntityValueTypeAsString
import app.shared.utils.UUID_Utils.EntityIdentity

case class UntypedRefWithoutVersion
(
  entityValueTypeAsString: EntityValueTypeAsString,
  entityIdentity:          EntityIdentity = EntityIdentity(),
)

