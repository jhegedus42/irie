package app.server.persistence.state

import app.shared.dataModel.value.ValueTypeAsString
import app.shared.dataModel.value.refs.EntityVersion
import app.shared.utils.UUID_Utils.IdentityOfEntity

case class UntypedRef(
                       entityType: ValueTypeAsString,
                       uuid:       IdentityOfEntity = IdentityOfEntity(),
                       version:    EntityVersion = EntityVersion()
)
