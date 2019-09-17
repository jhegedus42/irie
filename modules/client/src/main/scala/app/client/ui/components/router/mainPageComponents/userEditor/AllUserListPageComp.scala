package app.client.ui.components.router.mainPageComponents.userEditor

import app.client.ui.caching.cache.CacheEntryStates
import app.client.ui.caching.cacheInjector.{
  Cache,
  CacheAndProps,
  MainPageReactCompWrapper,
  ToBeWrappedMainPageComponent
}
import app.client.ui.components.router.mainPageComponents.{
  AllUserListPageWithCache,
  MainPage
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

import app.shared.entity.entityValue.values.User
import app.shared.entity.refs.RefToEntityWithoutVersion
import japgolly.scalajs.react.vdom.html_<^

trait AllUserListPageComp
    extends ToBeWrappedMainPageComponent[
      AllUserListPageComp,
      AllUserListPageWithCache
    ] {

  override type Props = AllUserListPageComp.Props
  override type Backend =
    AllUserListPageComp.Backend[AllUserListPageComp.Props]
  override type State = AllUserListPageComp.State

}

// todo-later
//  perhaps one could later give a bit more structure to pages like this
//  and formalize/enforce that structure in the ToBeWrappedComponent
//  trait, for example

object AllUserListPageComp {

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
      cacheInterfaceWrapper: CacheAndProps[Props],
      s:                     State
    ): VdomElement = {

      val renderLogic = RenderLogic(cacheInterfaceWrapper)

      <.div(
        <.br,
        <.hr,
        s"result for the GetAllUsersReq is:",
        <.br,
        renderLogic.listOfUsers.getOrElse(
          TagMod("List of users is loading ...")
        )
      )
    }

  }

  def getWrappedReactCompConstructor(
    cacheInterface:        Cache,
    propsProviderFunction: () => Props
  ) = {
    val reactCompWrapper =
      MainPageReactCompWrapper[
        AllUserListPageComp,
        AllUserListPageWithCache
      ](
        cache         = cacheInterface,
        propsProvider = propsProviderFunction,
        comp          = component
      )
    reactCompWrapper.wrappedConstructor
  }

  def getRoute(cacheInterface: Cache) = {
    dsl: RouterConfigDsl[MainPage] =>
      import dsl._

      def wrappedComponent(p: String) =
        getWrappedReactCompConstructor(
          cacheInterface,
          () => Props(p)
        )

      val sr: StaticDsl.StaticRouteB[MainPage, dsl.Rule] =
        staticRoute(
          "#userEditorPage",
          AllUserListPageWithCache("dummy string parameter")
        )

      sr ~> render({
        wrappedComponent("this is the prop")
      })

  }

  // todo-now - get a string from the URL and display it

  //
  //  def _tmp_userEditorPage(cacheInterface: CacheInterface) = {
  //    dsl: RouterConfigDsl[MainPage] =>
  //      import dsl._
  //
  //      val _userEditorPage = japgolly.scalajs.react.ScalaComponent
  //        .builder[UserEditorPage]("User editor page")
  //        .render(p => <.div(s"Info for user #${p.props.uuid}"))
  //        .build
  //
  //      //      dynamicRouteCT(
  //      //        "#app" / "user" / string("[a-zA-Z]+")
  //      //          .caseClass[UserEditorPage]
  //      //      ) ~> (dynRender(
  //      //        _userEditorPage(_: UserEditorPage)
  //      //      ))
  //
  //      dynamicRouteCT(
  //        "#app" / "user" / string("[a-zA-Z]+")
  //          .caseClass[UserEditorPage]
  //      ) ~> (dynRender({ paramForUserEditorPage: UserEditorPage =>
  //        //        _userEditorPage(paramForUserEditorPage)
  //
  //        SumNumbersPage.getWrappedReactCompConstructor(
  //          cacheInterface,
  //          () =>
  //            SumNumbersProps(
  //              s"hello world 42 + ${paramForUserEditorPage.uuid}"
  //            )
  //        )
  //
  //      }))
  //

}
