package app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.client

import app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.client.ViewCacheStates.{Loaded, LoadingCacheState, ViewCacheState}
import app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.shared.views.{Parameter, View}
import io.circe.{Decoder, Encoder}

import scala.reflect.ClassTag
import scala.util.{Failure, Success, Try}
//import scala.concurrent
import scala.concurrent.ExecutionContext.Implicits.global
//import app.shared.data.views.v5_type_dep_fun.ExecutionContexts.singleThreadedExecutionContext

case class Cache(ajaxInterface: AjaxInterface, pendingGetViewAjaxRequests: PendingGetViewAjaxRequests ) {

  var map: Map[Parameter, ViewCacheState[_]] = Map[Parameter, ViewCacheState[_]]()

  def getViewCacheState[V <: View: ClassTag: Encoder](
      params: V#Par
    )(
      implicit
      e: Encoder[V#Par],
      d: Decoder[V#Res]
    ): ViewCacheState[V] = {

    def registerHandlerOnGetViewAjaxRequestsFuture(ajaxRequest: GetViewAjaxRequest[V] ) = {
      def handleOnComplete(tryOptRes: Try[Option[V#Res]] ): Unit = {
//        println( "An GetViewAjaxRequest has completed, it returned with:" + tryOptRes )

        val res: Option[V#Res] = tryOptRes match {
          case Failure( exception ) => None
          case Success( value )     => value
        }

        if(res.nonEmpty)  {
            val x: V#Res = res.get
            val loaded = Loaded[V]( x )
            val newMap: Map[Parameter, ViewCacheState[_]] = map.updated( params, loaded )
            map=newMap
            // this is OK in JS - it is single threaded
            // also here we are using a single threaded execution context
            println("we updated the map, the new map is:"+map)
        }


        pendingGetViewAjaxRequests.handleGetViewAjaxRequestCompleted( ajaxRequest ) // 4

//        println("elertunk a handleOnComplete vegere, es ezt kaptuk: "+tryOptRes) // ez azert kell mert kulonben nem fut le
      }

//      println( "just before registering the handleOnComplete on the future" )
      ajaxRequest.ajaxResFuture.onComplete( {
        (x: Try[Option[V#Res]]) =>
          {
//            println( "ajaxGetViewReq.ajaxResFuture's completed" )
            handleOnComplete( x )
//            println(" the handleOnComplete( _ ) has returned")
          }
      } ) // we register the handler
    }

    val cacheStateOption: Option[ViewCacheState[_]] = map.get( params )

    val viewCacheState: ViewCacheState[V] = cacheStateOption match {
      case None => {

        val loadingViewCacheState: ViewCacheState[V] = LoadingCacheState( params )

        val newMap: Map[Parameter, ViewCacheState[_]] = map.updated( params, loadingViewCacheState )
        map = newMap

        val ajaxGetViewReq: GetViewAjaxRequest[V] = ajaxInterface.getAJAXGetViewRequest[V]( params ) // 2

        pendingGetViewAjaxRequests.addPendingAJAXRequest( ajaxGetViewReq )

        registerHandlerOnGetViewAjaxRequestsFuture(ajaxGetViewReq)

        loadingViewCacheState
      }

      case Some( vcs ) => {

        // to do some logic is needed to handle the Invalidated state

        val res: ViewCacheState[V] = vcs.asInstanceOf[ViewCacheState[V]]

//        val castedRes = res.asInstanceOf[V#Res]
//        Loaded( castedRes )
        res
      }

    }
    viewCacheState
  }

}
