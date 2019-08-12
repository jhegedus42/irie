package app.client.ui.components.router.mainPageComponents.adminPage

import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.vdom.html_<^._


object StaticAdminPage {


  import bootstrap4.TB.C

  private val component =
    ScalaComponent.builder
      .static("Static React Page Example")(
        //        <.div( Style.content, "Scala js react template, SBT 1.2.8" )
        <.div(
          <.main(C.container, ^.role := "container")
            (
              <.div(C.jumbotron)
                (
                <.h1("Static example"),
                <.p(C.lead, "paragraph")
                )
            )
          )
        )
      .build

  def apply(): Unmounted[Unit, Unit, Unit] = component()
}
