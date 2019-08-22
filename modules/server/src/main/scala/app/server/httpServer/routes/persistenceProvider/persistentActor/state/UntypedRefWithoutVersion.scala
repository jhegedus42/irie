package app.server.httpServer.routes.persistenceProvider.persistentActor.state

import app.shared.entity.asString.EntityValueTypeAsString
import app.shared.utils.UUID_Utils.EntityIdentity

case class UntypedRefWithoutVersion
(
  entityValueTypeAsString: EntityValueTypeAsString,
  entityIdentity:          EntityIdentity = EntityIdentity(),
)

