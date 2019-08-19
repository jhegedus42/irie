package app.shared.entity

import app.shared.entity.asString.EntityValueTypeAsString
import app.shared.entity.entityValue.EntityValue
import app.shared.utils.UUID_Utils.EntityIdentity
import monocle.macros.Lenses

@Lenses
case class RefToEntity[T <: EntityValue[T]](
    entityValueTypeAsString: EntityValueTypeAsString,
    entityIdentity:          EntityIdentity = EntityIdentity(),
    entityVersion:           EntityVersion = EntityVersion()
)

object RefToEntity {
}

