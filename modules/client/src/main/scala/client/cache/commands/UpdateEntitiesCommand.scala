package client.cache.commands

import io.circe.generic.JsonCodec
import shared.dataStorage.Value

@JsonCodec
case class UpdateEntitiesCommand[V <: Value[V]](
  updates: List[UpdateEntityInCacheCmd[V]])

object UpdateEntitiesCommand {

}
