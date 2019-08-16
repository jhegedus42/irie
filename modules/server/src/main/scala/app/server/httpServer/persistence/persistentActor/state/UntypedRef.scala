package app.server.httpServer.persistence.persistentActor.state

import app.shared.dataModel.value.{EntityValue, EntityValueTypeAsString}
import app.shared.dataModel.value.refs.{EntityVersion, RefToEntity}
import app.shared.utils.UUID_Utils.EntityIdentity

case class UntypedRef(
    entityValueTypeAsString: EntityValueTypeAsString,
    entityIdentity:          EntityIdentity = EntityIdentity(),
    entityVersion:           EntityVersion = EntityVersion()
)

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
