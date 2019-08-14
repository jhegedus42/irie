package app.client.ui.caching.entityCache

import app.shared.dataModel.value.EntityValue
import app.shared.dataModel.value.refs.{RefToEntity, Entity}

object EntityCacheStates {
  sealed trait EntityCacheState[E <: EntityValue[E]]{
    def isLoading: Boolean =
      this match {
        case Loading( _ )   => true
        case Loaded( _, _ ) => false
      }
  }
  case class Loading[E <: EntityValue[E]](r: RefToEntity[E] ) extends EntityCacheState[E]
  case class Loaded[E <: EntityValue[E]](r:  RefToEntity[E], refVal: Entity[E] ) extends EntityCacheState[E]


}
