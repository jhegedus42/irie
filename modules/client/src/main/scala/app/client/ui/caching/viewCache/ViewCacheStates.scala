package app.client.ui.caching.viewCache

import app.shared.data.model.View

object ViewCacheStates {
  sealed trait ViewCacheState[V <: View] {
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
  case class ViewLoading[V <: View](viewParam: V#Par ) extends ViewCacheState[V]

  case class ViewLoaded[V <: View](viewParam:  V#Par, viewResult: V#Res )
      extends ViewCacheState[V]

  case class ViewStale[V <: View](viewParam:  V#Par, viewResult: V#Res )
    extends ViewCacheState[V]

}
