package app.client.ui.components.mainPages.userHandling.userList

import app.client.ui.caching.cache.ReadCacheEntryStates
import app.client.ui.caching.cache.comm.write.WriteRequestHandlerTCImpl
import app.client.ui.caching.cacheInjector.{
  Cache,
  CacheAndPropsAndRouterCtrl,
  MainPageReactCompWrapper,
  ToBeWrappedMainPageComponent
}
import app.client.ui.components.mainPages.userHandling.userEditor.UserEditorComp.UserEditorPage
import app.client.ui.components.mainPages.userHandling.userList.UserListComp.Props
import app.client.ui.components.{
  MainPage,
  StaticTemplatePage,
  UserListPage
}
import app.shared.comm.{ReadRequest, WriteRequest}
import app.shared.comm.postRequests.read.{
  AdminPassword,
  GetAllUsersReq
}
import app.shared.comm.postRequests.{CreateEntityReq, GetEntityReq}
import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.values.User
import app.shared.entity.refs.RefToEntityWithVersion
import app.shared.initialization.testing.TestEntitiesForUsers
import app.shared.utils.UUID_Utils.EntityIdentity
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^.{<, ^, _}
import japgolly.scalajs.react.vdom.{VdomElement, html_<^}
import japgolly.scalajs.react.{
  BackendScope,
  Callback,
  CtorType,
  ScalaComponent
}
import org.scalajs.dom
import org.scalajs.dom.html.{Anchor, Div}

case class UserListRenderLogic(
  cacheAndPropsAndRouterCtrl: CacheAndPropsAndRouterCtrl[Props]) {

  // todo-now 1.1.1 - 1.1.1 - 1.1
  //  put a simple counter into this
  //  check out how this is done in the scalajs-react examples
  //  => we need to get rid of this stupid render Backend macro ...

  def getVDOM = {
    import bootstrap4.TB.convertableToTagOfExtensionMethods

    <.div(
      s"Users:",
      <.br,
      userListAsVDOM.getOrElse(
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

  def userListAsVDOM: Option[html_<^.TagMod] = {

    def refToAllUsersOption: Option[GetAllUsersReq.Res] = {
      def requestResultForRefToAllUsers
        : ReadCacheEntryStates.ReadCacheEntryState[ReadRequest,
                                                   GetAllUsersReq] =
        cacheAndPropsAndRouterCtrl.cache
          .readFromServer[ReadRequest, GetAllUsersReq](
            GetAllUsersReq.Par(AdminPassword("titok"))
          )
      requestResultForRefToAllUsers.toOption
    }

    def getUserListAsVDOM(
      l: List[Option[EntityWithRef[User]]]
    ): TagMod = {

      /**
        * todo-now - tenni ide egy linket
        * @param user
        * @return
        */
      def optUserEntity2VDOM(
        user: Option[EntityWithRef[User]]
      ): VdomElement = {
//        val ctl = cacheAndPropsAndRouterCtrl.props.routerCtl

        val ctl=cacheAndPropsAndRouterCtrl.routerCtl

        def linkToUserEditorPage(
          id: EntityIdentity[User]
        ): VdomTagOf[Anchor] =
          <.a("edit",
              ^.href := ctl.urlFor(StaticTemplatePage).value,
              ctl.setOnLinkClick(UserEditorPage(id.uuid)))

        if (user.isDefined) {
          val name = user.get.entityValue.name
          val n    = user.get.entityValue.favoriteNumber
          <.p(
            s"Name : $name , favorite number: $n ",
            linkToUserEditorPage(user.get.toRef.entityIdentity),
            <.br
          )
        } else <.div("user info not loaded yet ...")

      }

      //val l2= l.sortBy()

      // todo-later ... sort the users by name
      //  (or creation date, or something)

      TagMod(l.map(optUserEntity2VDOM(_)).toVdomArray)
    }

    def userRef2UserOption(
      r: RefToEntityWithVersion[User]
    ): GetEntityReq.Res[User] = {
      val par_ = GetEntityReq.Par(r)
      val res_ : Option[GetEntityReq.Res[User]] =
        cacheAndPropsAndRouterCtrl.cache
          .readFromServer[ReadRequest, GetEntityReq[User]](par_)
          .toOption
      val emptyResult: GetEntityReq.Res[User] =
        GetEntityReq.Res[User](None)

      res_.getOrElse(emptyResult)
    }

    for {
      res <- refToAllUsersOption
      res2 = RefToEntityWithVersion.getOnlyLatestVersions(
        res.allUserRefs
      )
      res3 = res2.map(userRef2UserOption(_))
      res4 = res3.map(x => x.optionEntity)
      res5 = getUserListAsVDOM(res4)
    } yield (res5)

  }

}
