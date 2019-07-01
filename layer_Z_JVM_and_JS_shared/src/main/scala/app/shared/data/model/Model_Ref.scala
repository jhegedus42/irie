package app.shared.data.model

import app.shared.data.model.Entity.{Entity}
import app.shared.data.ref.TypedRef

case class User(name: String ) extends Entity

case class LineList(user: TypedRef[User], name: String ) extends Entity

case class LineText(
    text:                             String,
    typedRefOpt_pointing_to_LineList: Option[TypedRef[LineList]] = None)
    extends Entity
