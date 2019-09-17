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
import app.client.ui.components.router.mainPageComponents.adminPage.StaticAdminPage
import app.client.ui.components.router.mainPageComponents.sumNumbers.{
  SumNumbersComponent,
  SumIntComp
}
import app.client.ui.components.router.mainPageComponents._
import app.client.ui.components.router.mainPageComponents.sumNumbers.SumIntComp.SumNumbersProps
import app.client.ui.components.router.mainPageComponents.userEditor.AllUserListPageComp
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

  val config = RouterConfigDsl[MainPage].buildConfig {
    dsl: RouterConfigDsl[MainPage] =>
      import dsl._

      val loginRoute
        : dsl.Rule = staticRoute(root, LoginPage) ~> render(
        LoginPageConstructor.component()
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

  val mainMenu = Vector.apply(
    Menu.apply("Home", LoginPage),
    Menu.apply("SumIntDemo - 137", SumIntPage(137)),
//    Menu.apply("User Editor", AllUserListPage("init string")),
    Menu.apply("ItemPage 4", ItemPage(4)),
    Menu.apply("ItemPage 42", ItemPage(42)),
    Menu.apply("Admin Page", AdminPage)
  )

  val baseUrl = BaseUrl.fromWindowOrigin_/

  val router = Router.apply(baseUrl, config)

  def layout(
    c: RouterCtl[MainPage],
    r: Resolution[MainPage]
  ) = {
    <.div.apply(
      TopNavComp.apply(TopNavComp.Props.apply(mainMenu, r.page, c)),
      r.render.apply(),
      FooterComp()
    )
  }

}
