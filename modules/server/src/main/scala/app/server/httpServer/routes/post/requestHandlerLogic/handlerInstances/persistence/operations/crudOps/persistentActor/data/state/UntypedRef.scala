package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations.crudOps.persistentActor.data.state

import app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations.crudOps.persistentActor.data.Payloads.UntypedRefWithoutVersion
import app.shared.entity.asString.EntityValueTypeAsString
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.refs.{EntityVersion, RefToEntityWithVersion}
import app.shared.utils.UUID_Utils.EntityIdentity
import monocle.macros.Lenses

@Lenses
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
      refToEntity: RefToEntityWithVersion[T]
  ): UntypedRef = {
    UntypedRef(
      entityValueTypeAsString = refToEntity.entityValueTypeAsString,
      entityIdentity          = refToEntity.entityIdentity,
      entityVersion           = refToEntity.entityVersion
    )
  }

}
