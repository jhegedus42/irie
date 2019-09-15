package app.client.ui.components.router.mainPageComponents.userEditor

import app.client.ui.caching.cache.CacheEntryStates
import app.client.ui.caching.cacheInjector.{
  CacheInterface,
  CacheInterfaceWrapper,
  ReactCompWrapper,
  ToBeWrappedComponent
}
import app.client.ui.components.router.mainPageComponents.{
  MainPage,
  UserEditorPage
}
import app.shared.comm.postRequests.{
  AdminPassword,
  GetAllUsersReq,
  GetEntityReq,
  SumIntRoute
}
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
import japgolly.scalajs.react.vdom.html_<^

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

      val sumOf13And42 = cacheInterfaceWrapper.cacheInterface
        .getPostReqResult[SumIntRoute](SumIntPar(13, 42))

      val requestResultForRefToAllUsers
        : CacheEntryStates.CacheEntryState[GetAllUsersReq] =
        cacheInterfaceWrapper.cacheInterface
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
          cacheInterfaceWrapper.cacheInterface
            .getPostReqResult[GetEntityReq[User]](par_)
            .toOption
        val emptyResult: GetEntityReq.Res[User] =
          GetEntityReq.Res[User](None)

        res_.getOrElse(emptyResult)
      }

      val resToDisplay: Option[html_<^.TagMod] = for {
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

      <.div(
        s"hello , 13+42 is :",
        <.br,
        s"${sumOf13And42}",
        <.br,
        <.hr,
        s"result for the GetAllUsersReq is:",
        <.br,
        resToDisplay.getOrElse(TagMod("List of users is loading ..."))
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


    // todo-now-2 - get a string from the URL and display it
    //
    //   use these comments for inspiration :
    //
    //   make this dynamic and pass props from the URL
    //   this should take a uuid and edit the user
    //   and write here the name of selected user
    //   use the comments below for inspiration
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
}
