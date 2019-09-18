package app.client.ui.components.router

import app.client.ui.caching.cacheInjector.{
  Cache,
  MainPageReactCompWrapper
}
import app.client.ui.components.generalComponents.TopNavComp.Menu
import app.client.ui.components.generalComponents.{
  FooterComp,
  TopNavComp
}
import app.client.ui.components.router.mainPageComponents.LoginPageComp.State.IsUserLoggedIn
import app.client.ui.components.router.mainPageComponents.adminPage.StaticAdminPage
import app.client.ui.components.router.mainPageComponents.sumNumbers.{
  SumIntComp
}
import app.client.ui.components.router.mainPageComponents._
import app.client.ui.components.router.mainPageComponents.userEditor.UserListComp
import japgolly.scalajs.react.{CtorType, ScalaComponent}
import japgolly.scalajs.react.extra.{OnUnmount, router}
import japgolly.scalajs.react.extra.router.StaticDsl.Rule
import japgolly.scalajs.react.extra.router.{
  Resolution,
  RouterConfigDsl,
  RouterCtl,
  _
}
import japgolly.scalajs.react.vdom.html_<^._

// this wrapper is needed so that we can "re render the react tree below this"
// it gets the re render triggerer so that it can update-it

object Pages {

  def itemPageRoute: RouterComp.RoutingRule = {
    dsl: RouterConfigDsl[MainPage] =>
      import dsl._

      val _itemPage = japgolly.scalajs.react.ScalaComponent
        .builder[ItemPage]("Item page")
        .render(p => <.div(s"Info for item #${p.props.id}"))
        .build

      dynamicRouteCT("item" / int.caseClass[ItemPage]) ~> (dynRender(
        _itemPage(_: ItemPage)
      ))

  }

}

case class RouterComp() {

  lazy val cache: Cache = new Cache()
  //  var refresher  = () => ()
  //  part of an eventual future hack -
  //  so this is left here

  val routerConfig: RouterConfig[MainPage] =
    RouterConfigDsl[MainPage].buildConfig {
      dsl: RouterConfigDsl[MainPage] =>
        import dsl._

        val loginRoute: dsl.Rule = staticRoute(root, LoginPage).~>(
          render(
            LoginPageComp.component()
          )
        )

        (trimSlashes
          | loginRoute
          | SumIntComp.getRoute(cache)(dsl)
          | Pages.itemPageRoute(dsl)
          | UserListComp.getRoute(cache)(dsl)
          | StaticAdminPage.getRoute(dsl))
          .notFound(
            redirectToPage(LoginPage)(Redirect.Replace)
          )
          .renderWith(f = layout)
    }

  def mainMenu: () => Vector[Menu] =
    () =>
      LoginPageComp.isUserLoggedIn match {
        case IsUserLoggedIn(true) =>
          Vector.apply(
            Menu.apply("Home", LoginPage),
            Menu.apply("SumIntDemo - 3845", SumIntPage(3845)),
            Menu.apply("User List and Editor", UserListPage("MezgaGeza")),
//            Menu.apply("ItemPage 4", ItemPage(4)),
//            Menu.apply("ItemPage 42", ItemPage(42)),
            Menu.apply("Admin Page", AdminPage)
          )
        case IsUserLoggedIn(false) =>
          Vector.apply(
            Menu.apply("Home", LoginPage)
          )
      }

  val baseUrl: BaseUrl = BaseUrl.fromWindowOrigin_/

  val routerComp
    : ScalaComponent[Unit, Resolution[MainPage], OnUnmount.Backend, CtorType.Nullary] =
    Router.apply(baseUrl, routerConfig)

  def layout(
    c: RouterCtl[MainPage],
    r: Resolution[MainPage]
  ) = {
    println("layout was called")

    // refresher = () => c.refresh.runNow()
    // this is a huge hack ... todo-later - "fix it"
    // this is here so that we can re-render the router when a user logs in

    // todo-later
    //  wait for gitter channal to try to answer a question on
    //  how to implement the "login" use case - using this router

    val tnc =
      TopNavComp.apply(TopNavComp.Props.apply(mainMenu, r.page, c))

    <.div.apply(
      tnc,
      r.render.apply(),
      FooterComp()
    )
  }

}

object RouterComp {
  type RoutingRule = RouterConfigDsl[MainPage] => Rule[MainPage]

}
