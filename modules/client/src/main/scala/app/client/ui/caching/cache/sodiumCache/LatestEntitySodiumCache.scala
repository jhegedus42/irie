package app.client.ui.caching.cache.sodiumCache

import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.EntityType
import app.shared.utils.UUID_Utils.EntityIdentity
import sodium._

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

trait SodiumEntityCache[V<:EntityType[V]] {

  val entityUpdatedStream = ???
  val entityInsertedStream = ???

  val collectionChangedStream = ???

  def update(ref:EntityWithRef[V]) = {
      ???
  }

  def insert(v:V) : Unit ={
    val key = ???
    val value = ???
  }

  type Key=EntityIdentity[V]
  type Value=EntityWithRef[V]
  val initMap=Map[Key,Value]()

  val cellMapLoop = new CellLoop[Map[Key,Value]]()

}
