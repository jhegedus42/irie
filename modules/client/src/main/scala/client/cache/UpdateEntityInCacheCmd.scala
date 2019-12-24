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
    val currentTypedRefVal: TypedReferencedValue[V] =
      updateEntityInCacheCmd.currentTypedReferencedValue

    val currentUntypedRefVal: UnTypedReferencedValue =
      UnTypedReferencedValue.fromTypedReferencedValue[V](
        currentTypedRefVal
      )

    val newVal =
      updateEntityInCacheCmd.newValue
    val newUntypedValue = UntypedValue.getFromValue(newVal)
    val updateEntityPersActCmd = UpdateEntityPersActCmd(
      currentUnTypedReferencedValue = currentUntypedRefVal,
      newUTPVal                     = newUntypedValue,
      requestState                  = RequestIsOnItsWayTowardsServer()
    )
    updateEntityPersActCmd

  }

}
