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

  lazy val cache: Cache = new Cache()

  def routerConfig: RouterConfig[MainPage] =
    RouterConfigDsl[MainPage].buildConfig {
      dsl: RouterConfigDsl[MainPage] =>
        import dsl._

        val loginRoute: dsl.Rule = staticRoute(root, LoginPage).~>(
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

        def route: RouterConfig[MainPage] ={
          val u = LoginPageComp.isUserLoggedIn.userOption.isDefined
          if(u) userIsLoggedIn
          else userIsNotLoggedIn
        }

        userIsLoggedIn.renderWith(f = RouterLayout.layout)

    }

  val baseUrl: BaseUrl = BaseUrl.fromWindowOrigin_/

  def routerComp =
    Router.apply(baseUrl, routerConfig)

}

object RouterComp {
  type RoutingRule = RouterConfigDsl[MainPage] => Rule[MainPage]

}
