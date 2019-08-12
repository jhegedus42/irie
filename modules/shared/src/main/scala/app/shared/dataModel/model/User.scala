package app.shared.dataModel.model

import app.shared.dataModel.entity.Entity

case class User( login: String, password : String, isAdmin: Boolean ) extends Entity[User]

// todo-now : CRUD users
// It is an "admin" page:
// steps:
//  (there is no admin user - it is just react page, that can simply
//  CRUD manipulate the users)
//  step 1 - Create
//  step 2 - List
//  step 3 - Edit
