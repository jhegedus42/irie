package app.client.ui.components.router

import app.client.ui.caching.cacheInjector.{Cache, MainPageReactCompWrapper}
import app.client.ui.components.generalComponents.TopNavComp.Menu
import app.client.ui.components.generalComponents.{FooterComp, TopNavComp}
import app.client.ui.components.router.mainPageComponents.LoginPageComp.State.IsUserLoggedIn
import app.client.ui.components.router.mainPageComponents.adminPage.StaticAdminPage
import app.client.ui.components.router.mainPageComponents.sumNumbers.{SumIntComp, SumNumbersComponent}
import app.client.ui.components.router.mainPageComponents._
import app.client.ui.components.router.mainPageComponents.sumNumbers.SumIntComp.SumNumbersProps
import app.client.ui.components.router.mainPageComponents.userEditor.AllUserListPageComp
import japgolly.scalajs.react.extra.router.{Resolution, RouterConfigDsl, RouterCtl, _}
import japgolly.scalajs.react.vdom.html_<^._

// this wrapper is needed so that we can "re render the react tree below this"
// it gets the re render triggerer so that it can update-it

object Pages {

  def itemPage = { dsl: RouterConfigDsl[MainPage] =>
    import dsl._

    val _itemPage = japgolly.scalajs.react.ScalaComponent
      .builder[ItemPage]("Item page")
      .render(p => <.div(s"Info for item #${p.props.id}"))
      .build

    dynamicRouteCT("item" / int.caseClass[ItemPage]) ~> (dynRender(
      _itemPage(_: ItemPage)
    ))

  }

  def sumIntRoute(cache: Cache) = {
//    dsl: RouterConfigDsl[MainPageWithCache[SumIntComp, SumIntPage]] =>
    dsl: RouterConfigDsl[MainPage] =>
      import dsl._

      def wrappedComp(page: SumIntPage) =
        SumIntComp.getWrappedReactCompConstructor(
          cache,
          () =>
            SumNumbersProps(
              s"hello world 42 and also, hello ${page.number}!"
            )
        )

      dynamicRouteCT("#item" / int.caseClass[SumIntPage]) ~> (dynRender(
        wrappedComp(_: SumIntPage)
      ))

  }

  def adminPage = { dsl: RouterConfigDsl[MainPage] =>
    import dsl._
    val adminPage
      : dsl.Rule = staticRoute("#admin", AdminPage) ~> render(
      StaticAdminPage.component()
    )

    adminPage
  }
}

case class RouterComp() {

  lazy val cache = new Cache()
  var refresher = () => ()

  val config = RouterConfigDsl[MainPage].buildConfig {
    dsl: RouterConfigDsl[MainPage] =>
      import dsl._

      val loginRoute
        : dsl.Rule = staticRoute(root, LoginPage) ~> render(
        LoginPageComp.component()
      )

      (trimSlashes
        | loginRoute
        | Pages.sumIntRoute(cache)(dsl)
        | Pages.itemPage(dsl)
        | AllUserListPageComp.getRoute(cache)(dsl)
        | Pages.adminPage(dsl))
        .notFound(
          redirectToPage(LoginPage)(Redirect.Replace)
        )
        .renderWith(f = layout)
  }

  def mainMenu: () => Vector[Menu] =  () => LoginPageComp.isUserLoggedIn match {
    case IsUserLoggedIn(true) =>
      Vector.apply(
        Menu.apply("Home", LoginPage),
        Menu.apply("SumIntDemo - 137", SumIntPage(137)),
        //    Menu.apply("User Editor", AllUserListPage("init string")),
        Menu.apply("ItemPage 4", ItemPage(4)),
        Menu.apply("ItemPage 42", ItemPage(42)),
        Menu.apply("Admin Page", AdminPage)
      )
    case IsUserLoggedIn(false) =>
      Vector.apply(
        Menu.apply("Home", LoginPage),
//        Menu.apply("SumIntDemo - 137", SumIntPage(137)),
        //    Menu.apply("User Editor", AllUserListPage("init string")),
//        Menu.apply("ItemPage 4", ItemPage(4)),
//        Menu.apply("ItemPage 42", ItemPage(42)),
//        Menu.apply("Admin Page", AdminPage)
      )
  }

  val baseUrl = BaseUrl.fromWindowOrigin_/

  val router = Router.apply(baseUrl, config)


  def layout(
    c: RouterCtl[MainPage],
    r: Resolution[MainPage]
  ) = {
    println("layout was called")

    refresher = () => c.refresh.runNow()
    // this is a huge hack ... todo-later - "fix it"
    // this is here so that we can re-render the router when a user logs in

    // todo-now-on-hold
    //  wait for gitter channal to try to answer a question on
    //  how to implement the "login" use case - using this router

    val tnc=TopNavComp.apply(TopNavComp.Props.apply(mainMenu, r.page, c))

    <.div.apply(
      tnc,
      r.render.apply(),
      FooterComp()
    )
  }

}
