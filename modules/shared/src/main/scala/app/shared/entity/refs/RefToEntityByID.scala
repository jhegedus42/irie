package app.shared.entity.refs

import app.shared.entity.asString.EntityValueTypeAsString
import app.shared.entity.entityValue.EntityType
import app.shared.utils.UUID_Utils.EntityIdentity
import monocle.macros.Lenses

@Lenses
case class RefToEntityByID[T <: EntityType[T]](
//    entityValueTypeAsString: EntityValueTypeAsString,
  entityIdentity: EntityIdentity[T] = EntityIdentity())
