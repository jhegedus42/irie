package app.client.ui.components.router

import app.client.ui.caching.cacheInjector.{Cache, MainPageReactCompWrapper}
import app.client.ui.components.{ItemPage, LoginPage, MainPage}
import app.client.ui.components.mainPageLayout.TopNavComp.Menu
import app.client.ui.components.mainPageLayout.{FooterComp, TopNavComp}
import app.client.ui.components.mainPages.LoginPageComp
import app.client.ui.components.mainPages.LoginPageComp.State.IsUserLoggedIn
import app.client.ui.components.mainPages.demos.{StaticTemplateComp, CacheDemoComp, TemplateComp}
import app.client.ui.components.mainPages.userHandling.{UserEditorComp, UserListComp}
import japgolly.scalajs.react.{CtorType, ScalaComponent}
import japgolly.scalajs.react.extra.{OnUnmount, router}
import japgolly.scalajs.react.extra.router.StaticDsl.Rule
import japgolly.scalajs.react.extra.router.{Resolution, RouterConfigDsl, RouterCtl, _}
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

  val routerConfig: RouterConfig[MainPage] =
    RouterConfigDsl[MainPage].buildConfig {
      dsl: RouterConfigDsl[MainPage] =>
        import dsl._

        val loginRoute: dsl.Rule = staticRoute(root, LoginPage).~>(
          renderR{
            x=>LoginPageComp.component(LoginPageComp.Props(x))
          }
        )

        (trimSlashes
          | loginRoute
          | CacheDemoComp.getRoute(cache)(dsl)
          | Pages.itemPageRoute(dsl)
          | UserEditorComp.getRoute(cache)(dsl)
          | TemplateComp.getRoute(cache)(dsl)
          | UserListComp.getRoute(cache)(dsl)
          | StaticTemplateComp.getRoute(dsl))
          .notFound(
            redirectToPage(LoginPage)(Redirect.Replace)
          )
          .renderWith(f = RouterLayout.layout)
    }

  val baseUrl: BaseUrl = BaseUrl.fromWindowOrigin_/

  val routerComp =
//    : ScalaComponent[Unit, Resolution[MainPage], OnUnmount.Backend, CtorType.Nullary] =
    Router.apply(baseUrl, routerConfig)

}

object RouterComp {
  type RoutingRule = RouterConfigDsl[MainPage] => Rule[MainPage]

}
