package app.client.ui.components.router

import app.client.ui.caching.cacheInjector.Cache
import app.client.ui.components.mainPages.demos.StaticTemplateComp
//import app.client.ui.components.mainPages.demos.{StaticTemplateComp, TemplateComp}
import app.client.ui.components.mainPages.login.LoginPageComp
import app.client.ui.components.mainPages.userHandling.userEditor.UserEditorRouteProvider
import app.client.ui.components.mainPages.userHandling.userList.UserListComp
import app.client.ui.components.{ItemPage, LoginPage, MainPage}
import japgolly.scalajs.react.extra.router.StaticDsl.Rule
import japgolly.scalajs.react.extra.router.{RouterConfigDsl, _}
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

  def routerConfig: RouterConfig[MainPage] =
    RouterConfigDsl[MainPage].buildConfig {
      dsl: RouterConfigDsl[MainPage] =>
        import dsl._

        val loginRoute: dsl.Rule = staticRoute(root, LoginPage).~>(
          renderR{
            x=>LoginPageComp.component(LoginPageComp.Props(x))
          }
        )

        val userIsLoggedIn=
        (trimSlashes
          | loginRoute
//          | ThieveryDemoComp.getRoute(cache)(dsl)
          | Pages.itemPageRoute(dsl)
//          | UserEditorRouteProvider.getRoute(cache)(dsl)
//          | TemplateComp.getRoute(cache)(dsl)
          | UserListComp.getStaticRoute(cache)(dsl)
          | StaticTemplateComp.getRoute(dsl))
          .notFound(
            redirectToPage(LoginPage)(Redirect.Replace)
          )

        // todo-later :
//        val userIsNotLoggedIn=
//          (trimSlashes
//            | loginRoute)
//            .notFound(
//              redirectToPage(LoginPage)(Redirect.Replace)
//            )
//
//          if(LoginPageComp.isUserLoggedIn.yesOrNo)
//          userIsLoggedIn
//            .renderWith(f = RouterLayout.layout)
//          else
//            userIsNotLoggedIn
//            .renderWith(f = RouterLayout.layout)

        userIsLoggedIn.renderWith(f = RouterLayout.layout)

    }

  val baseUrl: BaseUrl = BaseUrl.fromWindowOrigin_/

  def routerComp =
    Router.apply(baseUrl, routerConfig)

}

object RouterComp {
  type RoutingRule = RouterConfigDsl[MainPage] => Rule[MainPage]

}
