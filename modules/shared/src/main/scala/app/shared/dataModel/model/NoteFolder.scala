package app.shared.dataModel.model

import app.shared.dataModel.entity.Entity
import app.shared.dataModel.entity.refs.TypedRef

/**
  * This is a folder that contains the user's notes.
  *
  * @param user
  * @param name
  */
case class NoteFolder(user: TypedRef[User], name: String ) extends Entity[NoteFolder]
