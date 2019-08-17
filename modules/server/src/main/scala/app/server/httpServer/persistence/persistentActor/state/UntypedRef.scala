package app.server.httpServer.persistence.persistentActor.state

import app.shared.dataModel.value.EntityValue
import app.shared.dataModel.value.asString.EntityValueTypeAsString
import app.shared.dataModel.value.refs.{EntityVersion, RefToEntity}
import app.shared.utils.UUID_Utils.EntityIdentity

private[persistence] case class UntypedRef(
    entityValueTypeAsString: EntityValueTypeAsString,
    entityIdentity:          EntityIdentity = EntityIdentity(),
    entityVersion:           EntityVersion = EntityVersion()
)
{
  def asSimpleString():String={
      s"${this.entityIdentity.uuid} " +
      s"${this.entityValueTypeAsString.type_as_string} " +
      s"${entityVersion.versionNumberLong}"
  }
}

private[persistence] object UntypedRef {

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
