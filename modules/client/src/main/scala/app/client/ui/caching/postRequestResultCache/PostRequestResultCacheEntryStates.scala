package app.client.ui.caching.postRequestResultCache

import app.shared.comm.requests.Request

object PostRequestResultCacheEntryStates {
  sealed trait PostRequestResultCacheEntryState[V <: Request] {
    def isLoading: Boolean =
      this match {
        case PostRequestResultLoading( _ )   => true
        case PostRequestResultLoaded( _, _ ) => false
      }

    def toOption: Option[V#Res] =
      this match {
        case PostRequestResultLoading( _ )     => Option.empty
        case PostRequestResultLoaded( _, res ) => Some(res)
      }
  }
  case class PostRequestResultLoading[V <: Request](viewParam: V#Par ) extends PostRequestResultCacheEntryState[V]

  case class PostRequestResultLoaded[V <: Request](viewParam:  V#Par, viewResult: V#Res )
      extends PostRequestResultCacheEntryState[V]

  case class PostRequestResultStale[V <: Request](viewParam:  V#Par, viewResult: V#Res )
    extends PostRequestResultCacheEntryState[V]

}
