package client.ui.navigation

object TopNav {}

import japgolly.scalajs.react.vdom.html_<^.{<, ^}
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}
import scalacss.Defaults._
import scalacss.ScalaCssReact._
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import bootstrap4.TB.C
import client.sodium.core
import client.sodium.core.{Cell, StreamSink}
import client.ui.compositeWidgets.general.CellOptionDisplayerWidget
import org.scalajs.dom.html.{Element, LI}

object MenuItems {}

object NavigatorComp {

  lazy val selectPageByIDX: StreamSink[Int] = new StreamSink[Int]()

  lazy val selectPage: core.Stream[Option[Page]] =
    selectPageByIDX.map(Pages.pages.drop(_).headOption)

  lazy val selectedPage: Cell[Option[Page]] =
    selectPage.hold(Some(Pages.imgSeq))

  def handler(id: Int): TagMod = {
    def go(e: ReactMouseEvent): Callback = Callback {
      selectPageByIDX.send(id)
    }

    ^.onClick ==> go
  }

  private def pagesInNavbar(): TagMod = {

    def x: Int => VdomTagOf[LI] = { i: Int =>
      def res = <.li(
        C.navItem,
        C.navLink,
        C.pb1,
        C.pt0,
        <.a(
          C.pb1,
          C.pt0,
          C.navLink,
          s"${Pages.pages.toVector(i).name}",
          handler(i),
          ^.key := s"key_$i"
        )
      )
      res
    }

    val res1: TagMod = (0 to (Pages.pages.length-1)).map(x).toVdomArray
    res1

  }

  private def navBar(): VdomTagOf[Element] = {
    import bootstrap4.TB._
    <.header(
      <.nav(
        C.navbar,
        C.navbarDark,
        C.navbarExpandSm,
        C.mb4,
        C.bgDark,
        <.button.btn(
          C.navbarToggler,
          ^.`type` := "button",
          VdomAttr("data-toggle") := "collapse",
          VdomAttr("data-target") := "#navbarCollapse",
          ^.aria.controls := "navbarCollapse",
          ^.aria.expanded := "false",
          ^.aria.label := "toggle navigation",
          <.span(C.navbarTogglerIcon)
        ),
        <.div(
          C.collapse,
          C.navbarCollapse,
          ^.id := "navbarCollapse",
          <.ul(C.pt2, C.mrAuto, C.navbarNav, pagesInNavbar())
        )
      )
    )
  }

  lazy val pageDisplayer =
    CellOptionDisplayerWidget[Page](selectedPage, _.vdomTagOf)

  lazy val vdom=
    <.div(
      navBar(),
      <.br,
      <.div(
        <.main(C.container, ^.role := "container")(
          <.div(C.jumbotron)(
              pageDisplayer.optDisplayer(),
          )
        )
      )
    )

}
