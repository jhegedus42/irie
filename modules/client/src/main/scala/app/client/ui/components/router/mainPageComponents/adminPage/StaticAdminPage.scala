package app.client.ui.components.router.mainPageComponents.adminPage

import app.client.ui.components.router.RouterComp
import app.client.ui.components.router.mainPageComponents.{
  AdminPage,
  MainPage
}
import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.extra.router.RouterConfigDsl
import japgolly.scalajs.react.vdom.html_<^._

object StaticAdminPage {

  import bootstrap4.TB.C

  val component =
    ScalaComponent.builder
      .static("Admin page")(
        //        <.div( Style.content, "Scala js react template, SBT 1.2.8" )
        <.div(
          <.main(C.container, ^.role := "container")(
            <.div(C.jumbotron)(
              <.h1("Admin page"),
              <.p(C.lead, "This is just a demo for a static page.")
            )
          )
        )
      )
      .build

  def getRoute: RouterComp.RoutingRule = {
    dsl: RouterConfigDsl[MainPage] =>
      import dsl._
      val adminPage: dsl.Rule = staticRoute("#admin", AdminPage) ~>
        render(
          StaticAdminPage.component()
        )

      adminPage
  }
}
