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
case class UpdateEntitiesInCacheCmd[V <: Value[V]](
  currentTypedReferencedValue: TypedReferencedValue[V],
  newValue:                    V)

object UpdateEntitiesInCacheCmd {

  def toUpdateEntityPersActCmd[V <: Value[V]: Encoder: Typeable](
    updateEntityInCacheCmd: UpdateEntitiesInCacheCmd[V]
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
