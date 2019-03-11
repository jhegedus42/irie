package app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.client

import app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.shared.views.View

import scala.reflect.ClassTag

/**
  * Created by joco on 08/07/2018.
  */
case class PendingGetViewAjaxRequests(renderEngine:ReactRenderEngine) {

  var pendingRequests : Set[GetViewAjaxRequest[_<:View]] = Set()

  var isRenderingOnGoing : Boolean = false

  def handleGetViewAjaxRequestCompleted[V<:View:ClassTag](ajaxGetViewReq: GetViewAjaxRequest[V]) = {

//    println("pendingRequests filtereles előtt :"+pendingRequests)
//    pendingRequests=pendingRequests-ajaxGetViewReq // update mutable variable
    val filtered=pendingRequests.filter(x=> !x.uUID.id.equals(ajaxGetViewReq.uUID.id)) // ide kell vmi uuid
    pendingRequests=filtered
//    println("pendingRequests filtereles után :"+pendingRequests)


    if(!isRenderingOnGoing && pendingRequests.isEmpty){
//      println("PendingGetViewAjaxRequests calls renderEngine.render() in handleGetViewAjaxRequestCompleted()")
//      println("pendingRequests calls render()")
      renderEngine.render()
    }
    if(isRenderingOnGoing && pendingRequests.isEmpty){
      println("Illegal State ERROR, something is really not good, in handleGetViewAjaxRequestCompleted")
    }
  }


  def addPendingAJAXRequest[V<:View:ClassTag](ajaxGetViewReq: GetViewAjaxRequest[V]) = {
    pendingRequests=pendingRequests+ajaxGetViewReq
  }

  // event handlers for the ReactRenderEngine

  case class RenderingWillStartEventHandler() {
    def handleEvent() = {
      isRenderingOnGoing = true
    }
  }

  case class RenderingHasFinishedEventHandler() {
    def handleEvent() = {
      isRenderingOnGoing = false
    }
  }

  renderEngine.registerRenderingWillStartEventHandler(RenderingWillStartEventHandler())
  renderEngine.registerRenderingHasFinishedEventHandler(RenderingHasFinishedEventHandler())


}
