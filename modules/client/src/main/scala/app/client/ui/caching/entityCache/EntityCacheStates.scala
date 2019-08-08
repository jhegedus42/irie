package app.client.ui.caching.entityCache

import app.shared.entity.Entity.Entity
import app.shared.entity.{TypedRef, TypedRefVal}

object EntityCacheStates {
  sealed trait EntityCacheState[E <: Entity[E]]{
    def isLoading: Boolean =
      this match {
        case Loading( _ )   => true
        case Loaded( _, _ ) => false
      }
  }
  case class Loading[E <: Entity[E]](r: TypedRef[E] ) extends EntityCacheState[E]
  case class Loaded[E <: Entity[E]](r:  TypedRef[E], refVal: TypedRefVal[E] ) extends EntityCacheState[E]


}
