package app.client.ui.components.router.mainPageComponents.userEditor

import app.client.ui.caching.cache.CacheEntryStates
import app.client.ui.caching.cacheInjector.{CacheInterface, CacheInterfaceWrapper, ReactCompWrapper, ToBeWrappedComponent}
import app.client.ui.components.router.mainPageComponents.{MainPage, UserEditorPage}
import app.shared.comm.postRequests.{AdminPassword, GetAllUsersReq, GetEntityReq, SumIntRoute}
import app.shared.comm.postRequests.SumIntRoute.SumIntPar
import bootstrap4.TB.C
import japgolly.scalajs.react.extra.router.RouterConfigDsl
import japgolly.scalajs.react.{BackendScope, CtorType, ScalaComponent}
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, ^, _}
import io.circe.generic._
import java.io

import app.shared.entity.entityValue.values.User
import app.shared.entity.refs.RefToEntityWithoutVersion

trait UserEditorPageComp
    extends ToBeWrappedComponent[UserEditorPageComp] {

  override type Props = UserEditorPageComp.Props
  override type Backend =
    UserEditorPageComp.UserEditorBackend[UserEditorPageComp.Props]
  override type State = UserEditorPageComp.State

}

object UserEditorPageComp {

  case class State(stateString: String)
  case class Props(propsString: String)

  // this should take a uuid and edit the user

  // todo-now-3
  //  take uuid from URL and print user info
  //  such as: name + favorite number
  //

  val component: Component[
    CacheInterfaceWrapper[Props],
    State,
    UserEditorBackend[Props],
    CtorType.Props
  ] = {
    ScalaComponent
      .builder[CacheInterfaceWrapper[Props]](
        "User Editor Page"
      )
      .initialState(State("initial state"))
      .renderBackend[UserEditorBackend[Props]]
      .build
  }

  class UserEditorBackend[Properties](
    $ : BackendScope[CacheInterfaceWrapper[Properties], State]) {

    def render(
      cacheInterfaceWrapper: CacheInterfaceWrapper[Props],
      s:                     State
    ): VdomElement = {
      val res = cacheInterfaceWrapper.cacheInterface
        .getPostReqResult[SumIntRoute](SumIntPar(13, 42))

      val res2: CacheEntryStates.CacheEntryState[GetAllUsersReq] =
        cacheInterfaceWrapper.cacheInterface
          .getPostReqResult[GetAllUsersReq](
            GetAllUsersReq.Par(AdminPassword("titok"))
          )

      val res3: GetAllUsersReq.Res =
        res2.toOption.getOrElse(GetAllUsersReq.Res(List()))

      val res4: TagMod = TagMod(
        res3.allUserRefs
          .map(_.entityIdentity.uuid)
          .map(<.div(<.br, _))
          .toVdomArray
      )

      def userRef2UserOption(r:RefToEntityWithoutVersion[User])={
        val par: GetEntityReq.Par[User] = GetEntityReq.Par(r)
        val res= cacheInterfaceWrapper.cacheInterface.getPostReqResult()
        ???

      }

//      val res2_1=res2.toOption.map()

      <.div(
        s"hello , 13+42 is :",
        <.br,
        s"${res}",
        <.br,
        <.hr,
        s"result for the GetAllUsersReq is:",
        <.br,
        res4
      )
    }

  }

  def getWrappedReactCompConstructor(
    cacheInterface:       CacheInterface,
    propsProvderFunction: () => Props
  ) = {
    val reactCompWrapper = ReactCompWrapper[UserEditorPageComp](
      cache         = cacheInterface,
      propsProvider = propsProvderFunction,
      comp          = component
    )
    reactCompWrapper.wrappedConstructor
  }

  def getRoute(cacheInterface: CacheInterface) = {
    dsl: RouterConfigDsl[MainPage] =>
      import dsl._

      val wrappedComponent = getWrappedReactCompConstructor(
        cacheInterface,
        () => Props("hello user editor page")
      )

      staticRoute("#userEditorPage", UserEditorPage) ~> render({
        wrappedComponent
      })

    // todo-now => make this dynamic and pass props from the URL

  }
}
