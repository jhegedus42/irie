package app.client.ui.caching.cache

import app.shared.comm.PostRequest

object CacheEntryStates {
  sealed trait CacheEntryState[Req <: PostRequest] {
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
  case class Loading[V <: PostRequest](param: V#Par ) extends CacheEntryState[V]

  case class Loaded[V <: PostRequest](param:  V#Par, result: V#Res )
      extends CacheEntryState[V]

  case class Stale[V <: PostRequest](param:  V#Par, result: V#Res )
    extends CacheEntryState[V]

}
