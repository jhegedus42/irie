package app.shared.dataModel.entity.refs

import app.shared.dataModel.entity.TypeAsString
import app.shared.utils.UUID_Utils.UUID
import monocle.macros.Lenses

@Lenses
case class UnTypedRef(uuid: UUID = UUID.random(), dataType: TypeAsString)
