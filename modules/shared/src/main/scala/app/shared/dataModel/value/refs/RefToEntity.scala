package app.shared.dataModel.value.refs

import app.shared.dataModel.value.EntityValue
import app.shared.dataModel.value.asString.EntityValueTypeAsString
import app.shared.utils.UUID_Utils.EntityIdentity
import monocle.macros.Lenses
import io.circe.generic.auto._

import scala.reflect.ClassTag

@Lenses
case class RefToEntity[T <: EntityValue[T]](
    entityValueTypeAsString: EntityValueTypeAsString,
    entityIdentity:          EntityIdentity = EntityIdentity(),
    entityVersion:           EntityVersion = EntityVersion()
)
