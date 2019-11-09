package dataModel


import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.generic.JsonCodec
import refs.EntityType

@JsonCodec
case class User(
  name:           String,
  favoriteNumber: Int,
  password:       String="titok")
    extends EntityType[User]


// todo-next-0 admin page for users (CRUD for User Entity):
// steps:
//  (there is no admin user - it is just react page, that can simply
//  CRUD manipulate the users)
//  step 0 - List Users
//  step 1 - Create button => Create Page
//  step 2 - Edit/Details Button => Edit Page
