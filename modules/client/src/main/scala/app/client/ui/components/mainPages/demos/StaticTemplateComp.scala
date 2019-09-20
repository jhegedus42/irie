package app.client.ui.components.mainPages.demos

import app.client.ui.components.router.RouterComp
import app.client.ui.components.{StaticTemplatePage, MainPage}
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterConfigDsl
import japgolly.scalajs.react.vdom.html_<^._

object StaticTemplateComp {

  import bootstrap4.TB.C

  val component =
    ScalaComponent.builder
      .static("Static Demo Page")(
        //        <.div( Style.content, "Scala js react template, SBT 1.2.8" )
        <.div(
          <.main(C.container, ^.role := "container")(
            <.div(C.jumbotron)(
              <.h1("Static Demo Page"),
              <.p(C.lead, "This is just a demo for a static page.")
            )
          )
        )
      )
      .build

  def getRoute: RouterComp.RoutingRule = {
    dsl: RouterConfigDsl[MainPage] =>
      import dsl._
      val adminPage: dsl.Rule = staticRoute("#admin", StaticTemplatePage) ~>
        render(
          StaticTemplateComp.component()
        )

      adminPage
  }
}
