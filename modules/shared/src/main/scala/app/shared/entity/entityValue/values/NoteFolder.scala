package app.shared.entity.entityValue.values

import app.shared.entity.entityValue.EntityValue
import app.shared.entity.refs.RefToEntityWithVersion

/**
  * This is a folder that contains the user's notes.
  *
  * @param user
  * @param name
  */
case class NoteFolder(user: RefToEntityWithVersion[User], name: String )
    extends EntityValue[NoteFolder]
