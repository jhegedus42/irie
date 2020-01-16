package client.cache.commands

import io.circe.generic.JsonCodec
import shared.crudRESTCallCommands.persActorCommands.{
  UpdateEntitiesPersActorCmd,
  UpdateEntityPersActCmd
}

import io.circe.Encoder
import io.circe.generic.JsonCodec
import shapeless.Typeable
import shared.crudRESTCallCommands.RequestIsOnItsWayTowardsServer
import shared.crudRESTCallCommands.persActorCommands.UpdateEntityPersActCmd
import shared.dataStorage.{
  TypedReferencedValue,
  UnTypedReferencedValue,
  UntypedValue,
  Value
}
import shared.dataStorage.Value

@JsonCodec
case class UpdateEntitiesInCacheCommand[V <: Value[V]](
  updates: List[UpdateEntityInCacheCmd[V]])

object UpdateEntitiesInCacheCommand {

  def toUpdateEntityPersActCmd[V <: Value[V]: Encoder: Typeable](
    updateEntityInCacheCmd: UpdateEntitiesInCacheCommand[V]
  ): UpdateEntitiesPersActorCmd = {
    // todo-now
    val res: Seq[UpdateEntityPersActCmd] =
      updateEntityInCacheCmd.updates.map(
        UpdateEntityInCacheCmd.toUpdateEntityPersActCmd[V](_)
      )
    val l = res.toList
    UpdateEntitiesPersActorCmd(l, RequestIsOnItsWayTowardsServer())

  }

}
