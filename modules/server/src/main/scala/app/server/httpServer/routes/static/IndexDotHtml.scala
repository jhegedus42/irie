package app.server.httpServer.routes.static

import app.server.Config

private[routes] object IndexDotHtml {
  import scalatags.Text.all._
  import scalatags.Text.tags2.title

  def getIndexDotHTML: String = {

    val index_html =
      s"<!DOCTYPE html>" +
        html(
          lang := "en",
          head(
            title( "IM TEST PAGE" ),
            meta( charset := "utf-8" ),
            meta(
              name := "viewport",
              content := "width=device-width, initial-scale=1, shrink-to-fit=no"
            ),
            link(
              rel := "stylesheet",
              media := "screen",
              href := "./www/assets/stylesheets/bootstrap/bootstrap.min.css"
            )
          ),
          body( margin := 0 )(
            div( id := "rootComp" ),
            div( id := "testComp" ),
            script(
              `type` := "text/javascript"
            ),

            // todo - continue here ...
            // add config information into .js from
            // config.json on the server side

            script(
              `type` := "text/javascript",
              src := "./www/assets/js/bootstrap/popper.min.js"
            ),
            script(
              s"""
                |window.configObjFromServer={
                |     'port':${Config.configFromJSON.port},
                |     'host':'${Config.configFromJSON.ipForClient}'
                |    }
                |
                |""".stripMargin
            ),
            script(
              `type` := "text/javascript",
              src := "./www/assets/js/bootstrap/jquery-slim.min.js"
            ),

            script(
              `type` := "text/javascript",
              src := "./www/assets/js/bootstrap/bootstrap.min.js"
            ),

            script(
              `type` := "text/javascript",
              src := "./modules/client/target/scala-2.12/client-fastopt.js"
            ),

            script(
              `type` := "text/javascript",
              src := "./node/generated.js/bundle.js"
            ),

//            script("sourceMapSupport.install()"), //todo-later
            script( "Main().main()" ) // this starts the client
          )
        )
    index_html
  }

}
