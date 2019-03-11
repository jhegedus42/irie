package app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.client

/**
  * Created by joco on 08/07/2018.
  */
case class ReactRenderEngine() {

  type HasFinished=PendingGetViewAjaxRequests#RenderingHasFinishedEventHandler
  type WillStart=PendingGetViewAjaxRequests#RenderingWillStartEventHandler

  var reactComponentOpt : Option[ReactComponent] = None
  var renderingHasFinishedEventHandler:Option[HasFinished] = None

  var renderingWillStartEventHandler:Option[WillStart]=None

  def render() = {

    renderingWillStartEventHandler.map(v => v.handleEvent())

    def renderString(s: String):Unit = {
      println(
               "this is the rendered string by ReactRenderEngine's render() method =\n\n" + s +"\n\n"
             )
    }
    reactComponentOpt.map(
                           (rc: ReactComponent) => renderString(rc.getWhatToRender().string)
                         )
    renderingHasFinishedEventHandler.map(v=>v.handleEvent())
  }

  def setReactComponent(rc: ReactComponent)= this.reactComponentOpt=Some(rc)


  def registerRenderingWillStartEventHandler(
      handler:WillStart
    ) = this.renderingWillStartEventHandler=Some(handler)

  def registerRenderingHasFinishedEventHandler(
      handler: HasFinished
    ) = this.renderingHasFinishedEventHandler=Some(handler)

}
