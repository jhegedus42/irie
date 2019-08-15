package app.server.httpServer.persistence.state

import app.shared.dataModel.value.EntityValueTypeAsString
import app.shared.dataModel.value.refs.EntityVersion
import app.shared.utils.UUID_Utils.EntityIdentity

case class UntypedRef(
                       entityType: EntityValueTypeAsString,
                       uuid:       EntityIdentity = EntityIdentity(),
                       version:    EntityVersion = EntityVersion()
)
