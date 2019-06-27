package app.shared.data.model

import app.shared.data.model.Entity.{Entity, Value}
import app.shared.data.ref.TypedRef

case class User(name: String) extends Entity

case class UserLineList(user: TypedRef[User], name: String) extends Entity

case class LineText(text: String, typedRefOpt: Option[TypedRef[UserLineList]]= None) extends Entity

