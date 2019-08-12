package app.shared.dataModel.model

import app.shared.dataModel.entity.Entity
import app.shared.dataModel.entity.refs.TypedRef







case class Note(
    content: String,
    owner:   Option[TypedRef[NoteFolder]] = None
) extends Entity[Note]
