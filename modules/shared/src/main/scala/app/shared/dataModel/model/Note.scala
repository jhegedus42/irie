package app.shared.dataModel.model

import app.shared.dataModel.value.EntityValue
import app.shared.dataModel.value.refs.RefToEntity







case class Note(
    content: String,
    owner:   Option[RefToEntity[NoteFolder]] = None
) extends EntityValue[Note]
