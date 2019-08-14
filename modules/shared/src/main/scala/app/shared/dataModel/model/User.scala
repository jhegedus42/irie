package app.shared.dataModel.model

import app.shared.dataModel.entity.Entity

import io.circe._

case class User( name: String, favoriteNumber:Int ) extends Entity[User]



// todo-next admin page for users (CRUD for User Entity):
// steps:
//  (there is no admin user - it is just react page, that can simply
//  CRUD manipulate the users)
//  step 0 - List Users
//  step 1 - Create button
//  step 2 - Edit
