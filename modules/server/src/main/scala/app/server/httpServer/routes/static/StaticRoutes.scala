package app.server.httpServer.routes.static

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.server.Directives.{
  complete,
  getFromDirectory,
  pathSingleSlash,
  _
}
import akka.http.scaladsl.server.Route

/**
  * Created by joco on 29/04/2017.
  */
private[routes] object StaticRoutes {

  def getStaticRoute(rootPage: String ): Route = {
    val staticRoute: Route = {
      pathSingleSlash {
        import java.util.Calendar
        def time = Calendar.getInstance.getTime
        println( s"Someone asked for the root at $time" )
        complete {
          HttpResponse(
            entity = HttpEntity( ContentTypes.`text/html(UTF-8)`, rootPage )
          )
        }
      } ~
        getFromDirectory( "." ) // why is this here ? I don't know.
      // maybe this allows serving .css and .js
      // files, such as `bootstrap.min.css` etc ...

    }

    staticRoute
  }
}
