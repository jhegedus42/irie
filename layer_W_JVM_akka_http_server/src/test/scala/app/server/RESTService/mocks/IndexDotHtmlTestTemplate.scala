package app.server.RESTService.mocks

object IndexDotHtmlTestTemplate {
  import scalatags.Text.all._
  import scalatags.Text.tags2.title

  val jsModulesName          = "layer_V_JS_client"
  val jsModulesNameLowerCase = jsModulesName.map( x => x.toLower )

  def router_index(testClient: Boolean ) = {
    val testString   = if (testClient) "-test-" else "-"
    val packageName  = "app.client"
    val js_code_path = s"./${jsModulesName}/target/scala-2" + s".12/$jsModulesNameLowerCase${testString}fastopt.js"

    val index_html =
      s"<!DOCTYPE html>" +
        html(lang := "en",
          head(
            title( "IM TEST PAGE" ),
            meta( charset := "utf-8"),
            meta( name := "viewport", content := "width=device-width, initial-scale=1, shrink-to-fit=no" ),
            link(rel := "stylesheet",
              media := "screen",
              href := "./www/assets/stylesheets/bootstrap/bootstrap.min.css")
          ),
          body( margin := 0 )(
            div( id := "rootComp" ),
            script( `type` := "text/javascript", src := "./node/generated.js/bundle.js" ),
            script( `type` := "text/javascript", src := "./www/assets/js/bootstrap/popper.min.js" ),
            script( `type` := "text/javascript", src := "./www/assets/js/bootstrap/jquery-slim.min.js" ),
            script( `type` := "text/javascript", src := "./www/assets/js/bootstrap/bootstrap.min.js" ),
            script( `type` := "text/javascript", src := js_code_path ),
            script( s"${packageName}.Main().main()" )
          )
        )
    index_html
  }

}
