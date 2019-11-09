package app.client.ui.components.router

import app.client.ui.caching.cacheInjector.Cache
import app.client.ui.components.mainPages.generator.StaticTemplateComp
import app.client.ui.components.mainPages.pages.login.UserLoginStatus
import app.client.ui.components.{ListUsersAllNotesPage, UserListPage}
import app.client.ui.components.mainPages.pages.noteHandling
import app.client.ui.components.mainPages.pages.noteHandling.noteEditor.NoteEditorRouteProvider
import app.client.ui.components.mainPages.pages.noteHandling.userNoteList.ListUsersAllNotesComp
//import app.client.ui.components.mainPages.demos.{StaticTemplateComp, TemplateComp}
import app.client.ui.components.mainPages.pages.login.LoginPageComp
import app.client.ui.components.mainPages.pages.userHandling.userEditor.UserEditorRouteProvider
import app.client.ui.components.mainPages.pages.userHandling.userList.UserListComp
import app.client.ui.components.{ LoginPage, MainPage}
import japgolly.scalajs.react.extra.router.StaticDsl.Rule
import japgolly.scalajs.react.extra.router.{RouterConfigDsl, _}
import japgolly.scalajs.react.vdom.html_<^._


case class RouterComp() {
  import RouterComp.cache

  def routerConfigNotLoggedIng: RouterConfig[MainPage] =
    RouterConfigDsl[MainPage].buildConfig {
      dsl: RouterConfigDsl[MainPage] =>
        import dsl._

        def loginRoute: dsl.Rule = staticRoute(root, LoginPage).~>(
          renderR { x =>
            LoginPageComp.component(LoginPageComp.Props(x))
          }
        )

        def userIsNotLoggedIn =

          (trimSlashes
            | loginRoute)
            .notFound(
              redirectToPage(LoginPage)(Redirect.Replace)
            )

        userIsNotLoggedIn.renderWith(f = RouterLayout.layout)
    }

  def routerConfigLoggedIng: RouterConfig[MainPage] =
    RouterConfigDsl[MainPage].buildConfig {
      dsl: RouterConfigDsl[MainPage] =>
        import dsl._

        def loginRoute: dsl.Rule = staticRoute(root, LoginPage).~>(
          renderR { x =>
            LoginPageComp.component(LoginPageComp.Props(x))
          }
        )


        def userIsLoggedIn: RouterConfig[MainPage] =
          (trimSlashes
            | loginRoute

            | UserEditorRouteProvider.getRoute(cache)(dsl)

            | NoteEditorRouteProvider.getRoute(cache)(dsl)

            | UserListComp.getStaticRoute("userList", UserListPage())(
              cache
            )(dsl)

            | ListUsersAllNotesComp.getStaticRoute(
              "userNoteList",
              ListUsersAllNotesPage()
            )(cache)(dsl)
//            | StaticTemplateComp.getRoute(dsl)
            )
            .notFound(
              redirectToPage(LoginPage)(Redirect.Replace)
            )

            userIsLoggedIn.renderWith(f = RouterLayout.layout)

}

  def baseUrl: BaseUrl = BaseUrl.fromWindowOrigin_/

  def router =
    if(LoginPageComp.isUserLoggedIn.userOption.isDefined)
      Router.apply(baseUrl, routerConfigLoggedIng)
    else
      Router.apply(baseUrl, routerConfigNotLoggedIng)
}

object RouterComp {
  lazy val cache: Cache = new Cache()
  type RoutingRule = RouterConfigDsl[MainPage] => Rule[MainPage]

}
