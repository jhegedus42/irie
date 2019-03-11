package app.comm_model_on_the_server_side.serverSide

import app.comm_model_on_the_server_side.serverSide.akkaHttpWebServer.HttpServerOnTheInternet

//import app.model_to_be_moved_to_real_app.getViewCommunicationModel.serverSide.akkaHttpWebServer.HttpServerOnTheInternet

object Tester extends App {

  println("start")
  testServerSide()

  val server:          HttpServerOnTheInternet = HttpServerOnTheInternet()
  def testServerSide() = {

    // we put some stupid calls here

  }

  def testClientSide()={
    //    val ajaxInterface:   AjaxInterface           = AjaxInterface( server )
    //    val renderingEngine: ReactRenderEngine       = ReactRenderEngine()
    //    val pendingGetViewAjaxRequests: PendingGetViewAjaxRequests =
    //      PendingGetViewAjaxRequests( renderingEngine )
    //    val cache: Cache = Cache( ajaxInterface, pendingGetViewAjaxRequests )
    //    val reactComponent = ReactComponent( cache )
    //    renderingEngine.setReactComponent( reactComponent )
    //    renderingEngine.render()
    //    println("the main thread will sleep now for 20 sec")
    //    var counter=0
    //    do {
    //      Thread.sleep(1 * 1000)
    //      Thread.`yield`()
    //      println("yielding, "+counter)
    //      counter=counter+1
    //    } while(true)
    //    println("the main woke up - end of story")

  }
}
