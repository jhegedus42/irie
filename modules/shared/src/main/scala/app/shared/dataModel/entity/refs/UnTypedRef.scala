package app.shared.dataModel.entity.refs

import app.shared.dataModel.entity.EntityTypeAsString
import app.shared.utils.UUID_Utils.EntityUUID
import monocle.macros.Lenses

@Lenses
case class UnTypedRef(uuid: EntityUUID = EntityUUID(), dataType: EntityTypeAsString)
