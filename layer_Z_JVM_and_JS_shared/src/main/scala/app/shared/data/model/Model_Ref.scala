package app.shared.data.model

import app.shared.data.model.Entity.{Entity}
import app.shared.data.ref.TypedRef

case class User( name: String ) extends Entity

/**
  * This is a folder that contains the user's notes.
  *
  * @param user
  * @param name
  */
case class NoteFolder(user: TypedRef[User], name: String ) extends Entity

case class Note(
    content: String,
    owner:   Option[TypedRef[NoteFolder]] = None
) extends Entity
