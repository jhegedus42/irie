package app.client.ui.caching.entityCache

import app.shared.dataModel.value.EntityValue
import app.shared.dataModel.value.refs.{TypedRefToEntity, Entity}

object EntityCacheStates {
  sealed trait EntityCacheState[E <: EntityValue[E]]{
    def isLoading: Boolean =
      this match {
        case Loading( _ )   => true
        case Loaded( _, _ ) => false
      }
  }
  case class Loading[E <: EntityValue[E]](r: TypedRefToEntity[E] ) extends EntityCacheState[E]
  case class Loaded[E <: EntityValue[E]](r:  TypedRefToEntity[E], refVal: Entity[E] ) extends EntityCacheState[E]


}
