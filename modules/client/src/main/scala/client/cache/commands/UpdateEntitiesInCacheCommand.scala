package client.cache.commands

import shared.crudRESTCallCommands.persActorCommands.{UpdateEntitiesPersActorCmd, UpdateEntityPersActCmd}
import io.circe.Encoder
import io.circe.generic.JsonCodec
import shapeless.Typeable
import io.circe.Decoder.Result
import io.circe._
import io.circe.parser._
import io.circe.syntax._
import shapeless.Typeable
import shared.crudRESTCallCommands.RequestIsOnItsWayTowardsServer
import shared.dataStorage.model.Value

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
