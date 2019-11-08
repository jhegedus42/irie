package app.client.ui.caching.cache.sodiumCache

import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.EntityType

sealed trait EntityOperations[V<:EntityType[V]]{

}

case class CreateEntity[V<:EntityType[V]](v:V){
  def getEntityWithRef : EntityWithRef[V] = {
    ???
  }
}

case class UpdateEntity[V<:EntityType[V]](currentLatestEntityWithRef: EntityWithRef[V], newValue:V){
  def getUpdatedEntityWithRef : Option[EntityWithRef[V]] = {
    ???
  }

}

trait LatestEntitySodiumCache[V<:EntityType[V]] {

  val entityUpdatedStream = ???

  def update(ref:EntityWithRef[V]) = {
      ???
  }

}
