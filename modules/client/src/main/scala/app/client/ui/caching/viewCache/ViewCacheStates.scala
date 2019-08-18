package app.client.ui.caching.viewCache

import app.shared.comm.views.PostRequest

object ViewCacheStates {
  sealed trait ViewCacheState[V <: PostRequest] {
    def isLoading: Boolean =
      this match {
        case ViewLoading( _ )   => true
        case ViewLoaded( _, _ ) => false
      }

    def toOption: Option[V#Res] =
      this match {
        case ViewLoading( _ )     => Option.empty
        case ViewLoaded( _, res ) => Some(res)
      }
  }
  case class ViewLoading[V <: PostRequest](viewParam: V#Par ) extends ViewCacheState[V]

  case class ViewLoaded[V <: PostRequest](viewParam:  V#Par, viewResult: V#Res )
      extends ViewCacheState[V]

  case class ViewStale[V <: PostRequest](viewParam:  V#Par, viewResult: V#Res )
    extends ViewCacheState[V]

}
