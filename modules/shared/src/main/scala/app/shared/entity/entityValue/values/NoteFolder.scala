package app.shared.entity.entityValue.values

import app.shared.entity.RefToEntity
import app.shared.entity.entityValue.EntityValue

/**
  * This is a folder that contains the user's notes.
  *
  * @param user
  * @param name
  */
case class NoteFolder(user: RefToEntity[User], name: String ) extends EntityValue[NoteFolder]
