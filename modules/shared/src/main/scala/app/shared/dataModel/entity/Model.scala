package app.shared.dataModel.entity

import app.shared.dataModel.entity.Entity.Entity
import app.shared.dataModel.entity.refs.TypedRef

case class User( login: String, password : String, isAdmin: Boolean ) extends Entity[User]

// TODO CRUD PAGE FOR admin interface:
// TODO-NOW - create - TEST DATA : 3 USERS, Alice, Bob, Cecile, + admin (user)
//     => admin is a user
//
// todo next : admin can do the following (we don't care about authenticating yet)
//  there is one single admin user
//  and that user has an "admin" interface
//
// todo 1 - list them (users) ordered by their name in the client (in the admin interface)
// todo 2 - create a new user (create a React Page for This : login, password, Save, Cancel)
//     => 2.0
//        => create a TypeRef/TypeRefVal for an entity
//     => 2.1
//        => this will set the new user's user version to ZERO
//     => 2.2
//        => deny this requestion if user with this login exist already
//
//
// todo 3 - edit user (same react page as for todo-2 )
//     => this will increase the user's version by one
// todo 4 - delete a user (Are you sure ? Dialog : Yes / Cancel)
//     => this will increase the user's version by one
// todo 5 - make sure that :
//     in todos 3,4 :
//       if the user's version of the admin does not match the one
//       in the persistent actor, then deny the update request
//


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
