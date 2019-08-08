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
  case class Menu(name: String, route: MainPage )

  case class Props(
                    menus:        Vector[Menu],
                    selectedPage: MainPage,
                    ctrl:         RouterCtl[MainPage])

  implicit val currentPageReuse = Reusability.by_==[MainPage]
  implicit val propsReuse = Reusability.by( (_: Props).selectedPage )

  val buttonInNavbar= {

    import bootstrap4.TB._

//    <.button.navbarToggler(
//      ^.`type` := "button",
//      ^.
//      ^.aria.controls := C.navbarCollapse,
//      ^.aria.expanded := "false",
//      ^.aria.label := "Toggle Navigation")
//
//    )




  }

  private def pagesInNavbar(P: Props) = {
    P.menus.toTagMod { item: Menu =>
      <.li(
        C.navItem,
        C.navLink,
        ^.key := item.name,
        {
          if (P.selectedPage == item.route) C.active
          else C.navLink
        },
        //
        item.name,
        P.ctrl setOnClick item.route
      )
    }
  }

  private def navigatorOriginal(P: Props) = {
    import bootstrap4.TB._
    <.header(
      <.nav(C.navbar, C.navbarDark, C.mb4, C.navbarExpandMd, C.bgDark,

        <.button.btn(
          C.navbarToggler,
          ^.`type` := "button",
          VdomAttr("data-toggle") := "collapse",
          VdomAttr("data-target") := "#navbarCollapse",

          ^.aria.controls := "navbarCollapse",
          ^.aria.expanded := "false",
          ^.aria.label :="toggle navigation",
          <.span(C.navbarTogglerIcon)
        ),
        <.div(C.collapse,C.navbarCollapse, ^.id := "navbarCollapse",
          <.ul(C.navbarNav, C.mrAuto, pagesInNavbar(P))
        )
      )
    )
  }

  def navigatorFromOchrons(P:Props)=
    <.div(
      // here we use plain Bootstrap class names as these are specific to the top level layout defined here
      <.nav(^.className := "navbar navbar-inverse navbar-fixed-top",
        <.div(^.className := "container",
          <.div(^.className := "navbar-header", <.span(^.className := "navbar-brand", "SPA Tutorial")),
          <.div(^.className := "collapse navbar-collapse",
            pagesInNavbar(P),
            "ochrons"
          )
        )
      )
    )

  val component = ScalaComponent
    .builder[Props]( "TopNav" )
    .render_P { P: Props =>
      navigatorOriginal(P)
//      navigatorFromOchrons(P)
    }
    .configure( Reusability.shouldComponentUpdate )
    .build

  def apply(props: Props ) = component( props )

}
