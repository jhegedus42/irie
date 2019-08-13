package app.shared.dataModel.model

import app.shared.dataModel.entity.Entity

case class User( login: String, password : String, isAdmin: Boolean ) extends Entity[User]

// todo-now
// step 0 :
// server side, then starting up the server, add 3 users: Alice Bob Cili


// todo-next admin page for users (CRUD for User Entity):
// steps:
//  (there is no admin user - it is just react page, that can simply
//  CRUD manipulate the users)
//  step 0 - List Users
//  step 1 - Create button
//  step 2 - Edit
