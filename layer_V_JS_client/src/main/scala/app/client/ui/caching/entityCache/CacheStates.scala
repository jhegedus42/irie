package app.client.ui.caching.entityCache

import app.shared.data.model.Entity.Entity
import app.shared.data.ref.{RefVal, TypedRef}

object CacheStates {
  sealed trait CacheState[E <: Entity]{
    def isLoading: Boolean =
      this match {
        case Loading( _ )   => true
        case Loaded( _, _ ) => false
      }
  }
  case class Loading[E <: Entity](r: TypedRef[E] ) extends CacheState[E]
  case class Loaded[E <: Entity](r:  TypedRef[E], refVal: RefVal[E] ) extends CacheState[E]


}
