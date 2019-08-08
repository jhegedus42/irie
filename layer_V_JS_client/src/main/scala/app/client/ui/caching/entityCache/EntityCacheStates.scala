package app.client.ui.caching.entityCache

import app.shared.data.model.Entity.Entity
import app.shared.data.ref.{TypedRefVal, TypedRef}

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
