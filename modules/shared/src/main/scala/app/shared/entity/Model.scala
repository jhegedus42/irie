package app.shared.entity

import app.shared.entity.Entity.Entity

case class User( name: String ) extends Entity[User]

// TODO CRUD PAGE FOR THIS ^ :
// TODO-NOW - create - TEST DATA : 3 USERS, Alice, Bob, Cecile
// todo 1 - list them ordered by their name in the client
// todo 2 - create a new user (create a React Page for This : Name, Save, Cancel)
// todo 3 - change a user's name (create a React Page for This, NameField, Save, Cancel)
// todo 4 - delete a user (Are you sure ? Dialog : Yes / Cancel)
//  implement this deletion as a "deleted" flag, next to the "version" flag

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
