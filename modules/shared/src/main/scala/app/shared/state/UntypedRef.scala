package app.shared.state

import app.shared.entity.entityValue.EntityValue
import app.shared.entity.asString.EntityValueTypeAsString
import app.shared.entity.{EntityVersion, RefToEntity}
import app.shared.utils.UUID_Utils.EntityIdentity
import io.circe.generic.auto._

case class UntypedRef(
    entityValueTypeAsString: EntityValueTypeAsString,
    entityIdentity:          EntityIdentity = EntityIdentity(),
    entityVersion:           EntityVersion = EntityVersion()
) {

  def asSimpleString(): String = {
    s"${this.entityIdentity.uuid} " +
      s"${this.entityValueTypeAsString.type_as_string} " +
      s"${entityVersion.versionNumberLong}"
  }

  def stripVersion(): UntypedRefWithoutVersion =
    UntypedRefWithoutVersion( entityValueTypeAsString, entityIdentity )
}

object UntypedRef {

  def makeFromRefToEntity[T <: EntityValue[T]](
      refToEntity: RefToEntity[T]
  ): UntypedRef = {
    UntypedRef(
      entityValueTypeAsString = refToEntity.entityValueTypeAsString,
      entityIdentity          = refToEntity.entityIdentity,
      entityVersion           = refToEntity.entityVersion
    )
  }

}
