package app.server.httpServer.routes.logic.typeSafePersistenceHeaven.typelessHell.state.refs

import app.shared.entity.asString.EntityValueTypeAsString
import app.shared.utils.UUID_Utils.EntityIdentity

case class UntypedRefWithoutVersion
(
  entityValueTypeAsString: EntityValueTypeAsString,
  entityIdentity:          EntityIdentity = EntityIdentity(),
)

