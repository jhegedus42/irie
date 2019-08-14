package app.shared.dataModel.model

import app.shared.dataModel.value.EntityValue
import app.shared.dataModel.value.refs.RefToEntity

/**
  * This is a folder that contains the user's notes.
  *
  * @param user
  * @param name
  */
case class NoteFolder(user: RefToEntity[User], name: String ) extends EntityValue[NoteFolder]
