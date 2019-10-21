package app.client.ui.components.mainPages.userHandling.userList

import app.client.ui.caching.cache.comm.write.WriteRequestHandlerTCImpl
import app.client.ui.caching.cacheInjector.{
  Cache,
  CacheAndPropsAndRouterCtrl,
  MainPageReactCompWrapper,
  ToBeWrappedMainPageComponent
}
import app.client.ui.components.{
  MainPage,
  StaticTemplatePage,
  UserListPage
}
import app.shared.comm.WriteRequest
import app.shared.comm.postRequests.{CreateEntityReq, UpdateReq}
import app.shared.entity.entityValue.values.User
import app.shared.initialization.testing.TestEntitiesForUsers
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.{<, ^, _}
import japgolly.scalajs.react.{
  BackendScope,
  Callback,
  CtorType,
  ScalaComponent
}
import org.scalajs.dom

trait UserListComp
    extends ToBeWrappedMainPageComponent[
      UserListComp,
      UserListPage
    ] {

  override type PropsT = UserListComp.Props
  override type BackendT =
    UserListComp.Backend[UserListComp.Props]
  override type StateT = UserListComp.State

}

object UserListComp {

  case class State(someString: String)

  case class Props(routerCtl: RouterCtl[MainPage])

  val component: Component[
    CacheAndPropsAndRouterCtrl[Props],
    State,
    Backend[Props],
    CtorType.Props
  ] = {
    ScalaComponent
      .builder[CacheAndPropsAndRouterCtrl[Props]](
        "Page listing all the users"
      )
      .initialState(State("initial state"))
      .renderBackend[Backend[Props]]
      .build
  }


  class Backend[Properties](
    $ : BackendScope[CacheAndPropsAndRouterCtrl[Properties], State]) {

    val dummyButtonHandler = Callback({

      dom.window.alert("push the button, pu-push it real good !")
    })

    def dummyButtonHandler2(
      cacheAndPropsAndRouterCtrl: CacheAndPropsAndRouterCtrl[Props]
    ) =
      Callback({

//        dom.window.alert("push the button, pu-push it real good !")


        println("now we get into action")

        import io.circe.generic.auto._

        val entity: User = TestEntitiesForUsers.jetiLabnyom

        val params: CreateEntityReq.CreateEntityReqPar[User] =
          CreateEntityReq.CreateEntityReqPar(entity)

        implicit val writeReqHandler =
          WriteRequestHandlerTCImpl.CreateEntityReq.createUserEntityReqHandler

        println(
          s"we gonna send the params for creating a user: $params"
        )

        cacheAndPropsAndRouterCtrl.cache
          .writeToServer[WriteRequest, CreateEntityReq[User]](params)

      })

    def render(
      cacheAndPropsAndRouterCtrl: CacheAndPropsAndRouterCtrl[Props],
      s:                          State
    ): VdomElement = {

      println(
        "13784000-2EA3-4753-B2B7-DC8C3B54B417 - render was called in " +
          " app.client.ui.components.mainPages.userHandling.userList.UserListComp"
      )

      val renderLogic = UserListRenderLogic(
        cacheAndPropsAndRouterCtrl
      )
      val route = StaticTemplatePage

      import bootstrap4.TB.convertableToTagOfExtensionMethods

      <.div(
        s"Users:",
        <.br,
        renderLogic.userListAsVDOM.getOrElse(
          TagMod("List of users is loading ...")
        ),
        <.br,
        <.button.btn.btnPrimary(
          "Create new user.",
          ^.onClick --> dummyButtonHandler2(
            cacheAndPropsAndRouterCtrl
          )
        )
      )

    }

  }

  private def getWrappedComp(
    ctl:   RouterCtl[MainPage],
    cache: Cache
  ): MainPageReactCompWrapper[UserListComp, UserListPage] =
    MainPageReactCompWrapper[UserListComp, UserListPage](
      cache            = cache,
      propsProvider    = () => UserListComp.Props(ctl),
      comp             = UserListComp.component,
      routerController = ctl
    )

  def getRoute(cache: Cache) = {

    // todo-later forbid the "not login page" routes, like the one for this page
    //  if user is not logged in, for example, the following route should not
    //  work if the user is not logged in (problem = at the moment it does work):
    //  http://commserver.asuscomm.com:8080/#userList


    import japgolly.scalajs.react.extra.router._

    dsl: RouterConfigDsl[MainPage] =>
      import dsl._

      def r =
        staticRoute("#userList", UserListPage())

      def page2render: dsl.Renderer =
        Renderer(rc => getWrappedComp(rc, cache).wrappedConstructor)

      def res: dsl.Rule = (r).~>(page2render)

      res
  }

}
