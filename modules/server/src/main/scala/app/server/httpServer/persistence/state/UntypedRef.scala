package app.server.httpServer.persistence.state

import app.shared.dataModel.value.{EntityValue, EntityValueTypeAsString}
import app.shared.dataModel.value.refs.{EntityVersion, RefToEntity}
import app.shared.utils.UUID_Utils.EntityIdentity

case class UntypedRef(
    entityType: EntityValueTypeAsString,
    uuid:       EntityIdentity = EntityIdentity(),
    version:    EntityVersion = EntityVersion()
)

object UntypedRef {

  def makeFromRefToEntity[T <: EntityValue[T]](
      refToEntity: RefToEntity[T]
  ) :UntypedRef = { ??? } // todo-now-4

}
