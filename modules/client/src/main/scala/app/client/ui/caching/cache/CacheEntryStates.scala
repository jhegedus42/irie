package app.client.ui.caching.cache

import app.shared.comm.requests.Request

object CacheEntryStates {
  sealed trait CacheEntryState[Req <: Request] {
    def isLoading: Boolean =
      this match {
        case Loading( _ )   => true
        case Loaded( _, _ ) => false
      }

    def toOption: Option[Req#Res] =
      this match {
        case Loading( _ )     => Option.empty
        case Loaded( _, res ) => Some(res)
      }
  }
  case class Loading[V <: Request](param: V#Par ) extends CacheEntryState[V]

  case class Loaded[V <: Request](param:  V#Par, result: V#Res )
      extends CacheEntryState[V]

  case class Stale[V <: Request](param:  V#Par, result: V#Res )
    extends CacheEntryState[V]

}
