package app.server.RESTService

import akka.http.scaladsl.model.ContentType.WithCharset
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.server.Directives.{complete, getFromDirectory, pathSingleSlash, _}
import akka.http.scaladsl.server.{Route, StandardRoute}

/**
  * Created by joco on 29/04/2017.
  */


object StaticStuff {



  import java.util.Calendar

  def staticRootFactory(rootPage: String): Route = {
    val staticRoute: Route = {
      pathSingleSlash {
        extractClientIP { ip =>
          def address=ip.toOption.map(_.getHostAddress).getOrElse("unknown")
          def name=ip.toOption.map(_.getHostName).getOrElse("unknown")
          def time=Calendar.getInstance.getTime
          println(
            s"Someone asked for the root with " +
            s"ip=$address the time is $time, hostname:$name");
          complete {
            HttpResponse(entity = HttpEntity(ContentTypes.`text/html(UTF-8)`, rootPage))
          }
        }
      } ~
        getFromDirectory(".") // why is this here ? I don't know.
      // maybe this allows serving .css and .js
      // files, such as `bootstrap.min.css` etc ...

    }

    staticRoute
  }
}
