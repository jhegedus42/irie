package client.cache

import io.circe.Encoder
import io.circe.generic.JsonCodec
import shared.crudRESTCallCommands.RequestIsOnItsWayTowardsServer
import shared.crudRESTCallCommands.persActorCommands.{
  InsertEntityPersActCmd,
  UpdateEntityPersActCmd
}
import shared.dataStorage.{
  TypedReferencedValue,
  UnTypedReferencedValue,
  UntypedValue,
  Value
}
import shapeless.Typeable

@JsonCodec
case class UpdateEntityInCacheCmd[V <: Value[V]](
  currentTypedReferencedValue: TypedReferencedValue[V],
  newValue:                    V)

object UpdateEntityInCacheCmd {

  def toUpdateEntityPersActCmd[V <: Value[V]: Encoder: Typeable](
    updateEntityInCacheCmd: UpdateEntityInCacheCmd[V]
  ): UpdateEntityPersActCmd = {
    val trv: TypedReferencedValue[V] =
      updateEntityInCacheCmd.currentTypedReferencedValue

    val utrv: UnTypedReferencedValue =
      UnTypedReferencedValue.fromTypedReferencedValue[V](trv)

    val v   = trv.versionedEntityValue.valueWithoutVersion
    val utv = UntypedValue.getFromValue(v)
    val updateEntityPersActCmd = UpdateEntityPersActCmd(
      unTypedReferencedValue = utrv,
      newValue               = utv,
      requestState           = RequestIsOnItsWayTowardsServer()
    )
    updateEntityPersActCmd

  }

}
