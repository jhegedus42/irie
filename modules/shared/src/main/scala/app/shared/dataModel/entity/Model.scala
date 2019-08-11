package app.shared.dataModel.entity

import app.shared.dataModel.entity.Entity.Entity
import app.shared.dataModel.entity.refs.TypedRef

case class User( login: String, password : String, isAdmin: Boolean ) extends Entity[User]

// todo-next-4 : CRUD users
// It is an "admin" page:
// steps:
//  (there is no admin user - it is just react page, that can simply
//  CRUD manipulate the users)
//  step 1 - Create
//  step 2 - List
//  step 3 - Edit


/**
  * This is a folder that contains the user's notes.
  *
  * @param user
  * @param name
  */
case class NoteFolder(user: TypedRef[User], name: String ) extends Entity[NoteFolder]

case class Note(
    content: String,
    owner:   Option[TypedRef[NoteFolder]] = None
) extends Entity[Note]
