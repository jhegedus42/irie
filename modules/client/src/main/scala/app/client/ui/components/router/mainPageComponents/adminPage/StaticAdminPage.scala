package app.client.ui.components.router.mainPageComponents.adminPage

import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.vdom.html_<^._


object StaticAdminPage {


  import bootstrap4.TB.C

  val component =
    ScalaComponent.builder
      .static("Admin page")(
        //        <.div( Style.content, "Scala js react template, SBT 1.2.8" )
        <.div(
          <.main(C.container, ^.role := "container")
            (
              <.div(C.jumbotron)
                (
                <.h1("Admin page"),
                <.p(C.lead, "User CRUD comes here")

                  // todo-now-0 - CRUD user GUI
                  // todo-now-1 - list user names
                  // todo-now-2 - list user refs

                )
            )
          )
        )
      .build

  def apply(): Unmounted[Unit, Unit, Unit] = component()
}
