package app.client.ui.components.mainPageLayout
import app.client.ui.components.{
  ListUsersAllNotesPage,
  LoginPage,
  MainPage,
  UserListPage
}
import app.client.ui.components.mainPageLayout.TopNavComp.Menu
import app.client.ui.components.mainPages.pages.login.{
  LoginPageComp,
  UserLoginStatus
}
import app.client.ui.dom.Window
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import scalacss.Defaults._
import scalacss.ScalaCssReact._
import bootstrap4.TB.C
import org.scalajs.dom.html.LI

object MenuItems {

  def mainMenu: () => Vector[Menu] =
    () =>
      LoginPageComp.isUserLoggedIn match {

        case UserLoginStatus(Some(u)) =>
          Vector.apply(
            Menu.apply("Users", UserListPage()),
            Menu.apply("Notes", ListUsersAllNotesPage())
          )

        case UserLoginStatus(None) =>
          Vector.apply(
            Menu.apply("Login", LoginPage)
          )
      }

}

object TopNavComp {

  // this is used in the props
  case class Menu(
    name:  String,
    route: MainPage)

  case class Props(
    selectedPage: MainPage,
    ctrl:         RouterCtl[MainPage])

  private def pagesInNavbar(props: Props): TagMod = {

    def x: Menu => VdomTagOf[LI] = { item: Menu =>
      //      println(s" Start $item, ${item.route}")

      def res = <.li(
        C.navItem,
        C.navLink,
        C.pb1,
        C.pt0,
        <.a(C.pb1,
            C.pt0,
            C.navLink,
            item.name,
            ^.href := props.ctrl.urlFor(item.route).value),
        props.ctrl.setOnLinkClick(item.route),
        ^.key := item.name, {
          if (props.selectedPage == item.route) C.active
          else C.navLink
        }
      )
      res
      //      println(s" End: $item, ${item.route}")
    }

    def logout = {
      def item = Menu("Logout", LoginPage)

      //  CURRENT FOCUS => put this into the event handler for clicking the link

      def cb = props.ctrl.set(LoginPage) >> Callback {
        Window.setLoggedInUser(UserLoginStatus(None))
      }

      def handler: TagMod = {
        def go(e: ReactMouseEvent): Callback =
          CallbackOption.unless(ReactMouseEvent targetsNewTab_? e) >> cb
        ^.onClick ==> go
      }

      def res = <.li(
        C.navItem,
        C.navLink,
        C.pb1,
        C.pt0,
        <.a(C.pb1,
            C.pt0,
            C.navLink,
            item.name,
            ^.href := props.ctrl.urlFor(item.route).value),
        handler,
        ^.key := item.name
      )
      res
    }

    val res1: TagMod = MenuItems.mainMenu().toTagMod(x)

    val res2 =
      if (LoginPageComp.isUserLoggedIn.userOption.isDefined) {
        <.div(res1, logout)
      } else res1
    res2
  }

  private def navBar(P: Props) = {
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
          <.ul(C.pt2, C.mrAuto, C.navbarNav, pagesInNavbar(P))
        )
      )
    )
  }

  def component = ScalaComponent
    .builder[Props]("TopNav")
    .render_P { P: Props =>
      navBar(P)
    }
    .build

  def apply(props: Props) = component(props)

}
