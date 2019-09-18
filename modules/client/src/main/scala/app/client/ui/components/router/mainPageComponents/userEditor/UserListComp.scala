package app.client.ui.components.router.mainPageComponents.userEditor

import app.client.ui.caching.cache.CacheEntryStates
import app.client.ui.caching.cacheInjector.{
  Cache,
  CacheAndProps,
  MainPageReactCompWrapper,
  ToBeWrappedMainPageComponent
}
import app.client.ui.components.router.mainPageComponents.{
  MainPage,
  SumIntPage,
  UserListPage
}
import app.shared.comm.postRequests.{
  AdminPassword,
  GetAllUsersReq,
  GetEntityReq,
  SumIntRoute
}
import app.shared.comm.postRequests.SumIntRoute.SumIntPar
import bootstrap4.TB.C
import japgolly.scalajs.react.extra.router.{
  RouterConfigDsl,
  StaticDsl
}
import japgolly.scalajs.react.{BackendScope, CtorType, ScalaComponent}
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, ^, _}
import io.circe.generic._
import java.io

import app.client.ui.components.router.mainPageComponents.sumNumbers.SumIntComp
import app.client.ui.components.router.mainPageComponents.sumNumbers.SumIntComp.StateAndProps.SumNumbersProps
import app.client.ui.components.router.{Pages, RouterComp}
import app.shared.entity.entityValue.values.User
import app.shared.entity.refs.RefToEntityWithoutVersion
import japgolly.scalajs.react.vdom.html_<^

trait UserListComp
    extends ToBeWrappedMainPageComponent[
      UserListComp,
      UserListPage
    ] {

  override type Props = UserListComp.Props
  override type Backend =
    UserListComp.Backend[UserListComp.Props]
  override type State = UserListComp.State

}

// todo-later
//  perhaps one could later give a bit more structure to pages like this
//  and formalize/enforce that structure in the ToBeWrappedComponent
//  trait, for example

object UserListComp {

  case class State(someString: String)

  case class Props(someString: String)

  val component: Component[
    CacheAndProps[Props],
    State,
    Backend[Props],
    CtorType.Props
  ] = {
    ScalaComponent
      .builder[CacheAndProps[Props]](
        "Page listing all the users"
      )
      .initialState(State("initial state"))
      .renderBackend[Backend[Props]]
      .build
  }

  case class RenderLogic(
    cacheInterfaceWrapper: CacheAndProps[Props]) {

    val requestResultForRefToAllUsers
      : CacheEntryStates.CacheEntryState[GetAllUsersReq] =
      cacheInterfaceWrapper.cache
        .getPostReqResult[GetAllUsersReq](
          GetAllUsersReq.Par(AdminPassword("titok"))
        )

    val refToAllUsersOption: Option[GetAllUsersReq.Res] =
      requestResultForRefToAllUsers.toOption

    def listOfStrings2TagMod(l: List[String]): TagMod =
      TagMod(l.map(<.div(<.br, _)).toVdomArray)

    def userRef2UserOption(
      r: RefToEntityWithoutVersion[User]
    ): GetEntityReq.Res[User] = {
      val par_ = GetEntityReq.Par(r)
      val res_ =
        cacheInterfaceWrapper.cache
          .getPostReqResult[GetEntityReq[User]](par_)
          .toOption
      val emptyResult: GetEntityReq.Res[User] =
        GetEntityReq.Res[User](None)

      res_.getOrElse(emptyResult)
    }

    val listOfUsers: Option[html_<^.TagMod] = for {
      res <- refToAllUsersOption
      res2 = res.allUserRefs
      res3 = res2.map(userRef2UserOption(_))
      res4 = res3.map(
        x =>
          x.optionEntity
            .map(_.entityValue.name)
            .getOrElse("name not loaded yet ...")
      )
      res5 = listOfStrings2TagMod(res4)
    } yield (res5)

  }

  class Backend[Properties](
    $ : BackendScope[CacheAndProps[Properties], State]) {

    def render(
      cacheAndProps: CacheAndProps[Props],
      s:             State
    ): VdomElement = {

      val renderLogic = RenderLogic(cacheAndProps)

      <.div(
        <.br,
        <.hr,
        s"result for the GetAllUsersReq is:",
        <.br,
        renderLogic.listOfUsers.getOrElse(
          TagMod("List of users is loading ...")
        ),
        <.br,
        <.p(
          s"This is what we got from the " +
            s"URL : ${cacheAndProps.props.someString}"
        )
      )

      // todo-now
      //  add some button here, which, when clicked will redirect/navigate
      //  to a given UserListPage, with some given urlParam ( which will be
      //  an UUID for an User, ultimately, once we get to the end of this
      //  but for now some arbitrary random string will do - as proof of
      //  concept )

    }

  }

  def getRoute(cacheInterface: Cache): RouterComp.RoutingRule = {

    dsl: RouterConfigDsl[MainPage] =>
      import dsl._
      dynamicRouteCT(
        "#userList" / string("[a-zA-Z]+").caseClass[UserListPage]
      ) ~> dynRender { page: UserListPage =>
        {
          MainPageReactCompWrapper[UserListComp, UserListPage](
            cache = cacheInterface,
            propsProvider =
              () => UserListComp.Props(page.paramFromURL),
            comp = UserListComp.component
          ).wrappedConstructor
        }
      }

  }
}
