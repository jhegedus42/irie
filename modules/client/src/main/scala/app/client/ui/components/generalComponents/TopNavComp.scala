package app.client.ui.components.generalComponents
import app.client.ui.components.router.mainPageComponents.MainPage
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.Reusability
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import scalacss.Defaults._
import scalacss.ScalaCssReact._
import bootstrap4.TB.C

object TopNavComp {

  // this is used in the props
  case class Menu( name: String, route: MainPage )

  case class Props(
      menus:        Vector[Menu],
      selectedPage: MainPage,
      ctrl:         RouterCtl[MainPage]
  )

  implicit val currentPageReuse = Reusability.by_==[MainPage]
  implicit val propsReuse       = Reusability.by( (_: Props).selectedPage )

  val buttonInNavbar = {

    import bootstrap4.TB._

  }

  private def pagesInNavbar( P: Props ) = {
    P.menus.toTagMod { item: Menu =>
      <.li(
        C.navItem,
        C.navLink,
        C.pb1,C.pt0,
        <.a(C.pb1,C.pt0, C.navLink, item.name, ^.href := P.ctrl.urlFor(item.route).value),
        P.ctrl.setOnLinkClick(item.route),
        ^.key := item.name, {
          if (P.selectedPage == item.route) C.active
          else C.navLink
        },
      )
    }
  }

  private def navigatorOriginal( P: Props ) = {
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
            VdomAttr( "data-toggle" ) := "collapse",
            VdomAttr( "data-target" ) := "#navbarCollapse",
            ^.aria.controls := "navbarCollapse",
            ^.aria.expanded := "false",
            ^.aria.label := "toggle navigation",
            <.span( C.navbarTogglerIcon )
          ),
          <.div(
            C.collapse,
            C.navbarCollapse,
            ^.id := "navbarCollapse",
            <.ul(C.pt2, C.mrAuto, C.navbarNav,   pagesInNavbar( P ) )
        )
      )
    )
  }


  val component = ScalaComponent
    .builder[Props]( "TopNav" )
    .render_P { P: Props =>
      navigatorOriginal( P )
    }
    .configure( Reusability.shouldComponentUpdate )
    .build

  def apply( props: Props ) = component( props )

}
