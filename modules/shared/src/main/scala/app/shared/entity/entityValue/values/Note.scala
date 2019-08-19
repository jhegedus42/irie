package app.shared.entity.entityValue.values

import app.shared.entity.RefToEntity
import app.shared.entity.entityValue.EntityValue


case class Note(
    content: String,
    owner:   Option[RefToEntity[NoteFolder]] = None
) extends EntityValue[Note]
