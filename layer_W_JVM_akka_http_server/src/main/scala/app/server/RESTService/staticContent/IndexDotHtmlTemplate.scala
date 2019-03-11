package app.server.RESTService.staticContent

object IndexDotHtmlTemplate{
  import scalatags.Text.all._
  import scalatags.Text.tags2.title

  val txt =
    "<!DOCTYPE html>" +
      html(
        head(
          title("IM - NOT TEST"),
          meta(httpEquiv:="Content-Type", content:="text/html; charset=UTF-8"),
          link(rel:="stylesheet",media:="screen",href:="./www/assets/stylesheets/react-sortable-hoc-styles.css"),
          link(rel:="stylesheet",media:="screen",href:="./www/assets/stylesheets/general.css"),
          link(rel:="stylesheet",media:="screen",href:="./www/assets/stylesheets/ReactCrop.css")
//          link(rel:="stylesheet",media:="screen",href:="./server/target/web/less/main/stylesheets/main.min.css")

//  <link rel="stylesheet"    media= "screen"   href=@_asset("stylesheets/react-sortable-hoc-styles.css") >
        ),
        body(margin:=0)(
          div(id:="joco"),
          div(id:="jsReactComp"),
          div(id:="jsReactCrop"),
          script(`type`:="text/javascript", src:="./node/generated.js/index-bundle.js"),
          script(`type`:="text/javascript", src:="./js/target/scala-2.11/js-fastopt.js"),
          script(`type`:="text/javascript", src:="./js/target/scala-2.11/js-jsdeps.js"),
          script("app.client.Main().main()")//, //start scalajs-react app
//          script("experiments.cache.ExperimentalAppWithCache().main()")//, //start scalajs-react app
//          script("console.log('rubadub 42')") //start scalajs-react app
        )
      )
}




