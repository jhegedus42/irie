package app.server.persistence.state

import app.shared.dataModel.entity.EntityTypeAsString
import app.shared.dataModel.entity.refs.Version
import app.shared.utils.UUID_Utils.EntityUUID

case class UntypedRef(
    entityType: EntityTypeAsString,
    uuid:       EntityUUID = EntityUUID(),
    version:    Version = Version()
)
