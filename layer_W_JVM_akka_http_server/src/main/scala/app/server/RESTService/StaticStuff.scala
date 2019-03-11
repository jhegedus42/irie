package app.server.RESTService

import akka.http.scaladsl.model.ContentType.WithCharset
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.server.Directives.{complete, getFromDirectory, pathSingleSlash, _}
import akka.http.scaladsl.server.{Route, StandardRoute}
/**
  * Created by joco on 29/04/2017.
  */


object StaticStuff {

  def staticRootFactory(rootPage: String ): Route = {
    val ct: WithCharset = ContentTypes.`text/html(UTF-8)`
    val root: StandardRoute =
      complete {
        HttpResponse( entity = HttpEntity( ct, rootPage ) )
      }

    val getStaticStuff: Route = {
      pathSingleSlash {
        root
      } ~
        getFromDirectory( "." ) //~
      //          pathPrefix(CommonStrings.imageRoute) { getFromDirectory(imgDir) } ~
      //          pathPrefix(CommonStrings.imageRoute) { getFromDirectory("tmp") }
    }
    getStaticStuff
  }
}
