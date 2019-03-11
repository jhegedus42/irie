package app.client.comm.cache.exposed

import app.shared.data.model.Entity.Entity
import app.shared.data.ref.{Ref, RefVal}

object CacheStates {
  sealed trait CacheState[E <: Entity]{
    def isLoading: Boolean =
      this match {
        case Loading( _ )   => true
        case Loaded( _, _ ) => false
      }
  }
  case class Loading[E <: Entity](r: Ref[E] ) extends CacheState[E]
  case class Loaded[E <: Entity](r:  Ref[E], refVal: RefVal[E] ) extends CacheState[E]


}
