package app.shared.dataModel.model

import app.shared.dataModel.value.EntityValue
import app.shared.dataModel.value.refs.TypedRefToEntity







case class Note(
    content: String,
    owner:   Option[TypedRefToEntity[NoteFolder]] = None
) extends EntityValue[Note]
