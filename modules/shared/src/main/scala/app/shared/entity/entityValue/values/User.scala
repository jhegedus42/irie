package app.shared.entity.entityValue.values

import app.shared.entity.entityValue.EntityValue

import io.circe._

case class User( name: String, favoriteNumber:Int ) extends EntityValue[User]



// todo-next-0 admin page for users (CRUD for User Entity):
// steps:
//  (there is no admin user - it is just react page, that can simply
//  CRUD manipulate the users)
//  step 0 - List Users
//  step 1 - Create button => Create Page
//  step 2 - Edit/Details Button => Edit Page