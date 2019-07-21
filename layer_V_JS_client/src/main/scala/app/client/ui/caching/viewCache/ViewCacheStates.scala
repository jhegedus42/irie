package app.client.ui.caching.viewCache

import app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.shared.views.View

object ViewCacheStates {
  sealed trait ViewCacheState[V <: View] {
    def isLoading: Boolean =
      this match {
        case ViewLoading( _ )   => true
        case ViewLoaded( _, _ ) => false
      }
  }
  case class ViewLoading[V <: View](viewParam: V#Par ) extends ViewCacheState[V]

  case class ViewLoaded[V <: View](viewParam:  V#Par, viewResult: V#Res )
      extends ViewCacheState[V]

  case class ViewStale[V <: View](viewParam:  V#Par, viewResult: V#Res )
    extends ViewCacheState[V]

}
