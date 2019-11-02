package app.client.ui.components.mainPages.pages.userHandling.userList

import app.client.ui.caching.cache.ReadCacheEntryStates
import app.client.ui.caching.cache.comm.read.ReadCache
import app.client.ui.caching.cache.comm.write.WriteRequestHandlerTCImpl
import app.client.ui.caching.cacheInjector.{
  Cache,
  CacheAndPropsAndRouterCtrl,
  MainPageReactCompWrapper,
  ToBeWrappedMainPageComponent
}
import app.client.ui.components.mainPages.helpers.ListRenderHelper
import app.client.ui.components.mainPages.pages.userHandling.userEditor.UserEditorComp.UserEditorPage
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
import app.shared.entity.entityValue.EntityType
import app.shared.entity.entityValue.values.User
import app.shared.entity.refs.RefToEntityWithVersion
import app.shared.initialization.testing.TestEntitiesForUsers
import app.shared.utils.UUID_Utils.EntityIdentity
import io.circe.{Decoder, Encoder}
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

import scala.reflect.ClassTag

case class UserListRenderLogic[Props](
  cacheAndPropsAndRouterCtrl: CacheAndPropsAndRouterCtrl[Props]) {

  val helper =
    ListRenderHelper[User, Props](cacheAndPropsAndRouterCtrl)

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
        ^.onClick --> createNewEntityHandler(
          cacheAndPropsAndRouterCtrl
        )
      )
    )

  }

  def createNewEntityHandler(
    cacheAndPropsAndRouterCtrl: CacheAndPropsAndRouterCtrl[Props]
  ) =
    Callback({

      //        dom.window.alert("push the button, pu-push it real good !")

      println("now we get into action")

      import io.circe.generic.auto._

      val entity: User = TestEntitiesForUsers.jetiLabnyom

      val params: CreateEntityReq.CreateEntityReqPar[User] =
        CreateEntityReq.CreateEntityReqPar(entity)

//      implicit val writeReqHandler
//        : WriteRequestHandlerTCImpl[CreateEntityReq[User]] = ???

      println(
        s"we gonna send the params for creating a user: $params"
      )

      cacheAndPropsAndRouterCtrl.cache
        .writeToServer[CreateEntityReq[User]](params)

    })

  def refToAllUsersOption: Option[GetAllUsersReq.Res] = {

    def requestResultForRefToAllUsers
      : ReadCacheEntryStates.ReadCacheEntryState[GetAllUsersReq] =
      cacheAndPropsAndRouterCtrl.cache
        .readFromServer[GetAllUsersReq](
          GetAllUsersReq.Par(AdminPassword("titok"))
        )

    requestResultForRefToAllUsers.toOption
  }

  /**
    * todo-now - tenni ide egy linket
    * @param user
    * @return
    */
  def optEntity2VDOM(
    user: Option[EntityWithRef[User]]
  ): VdomElement = {
    //        val ctl = cacheAndPropsAndRouterCtrl.props.routerCtl

    val ctl = cacheAndPropsAndRouterCtrl.routerCtl

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

  def userListAsVDOM: Option[html_<^.TagMod] = {

    for {
      res <- refToAllUsersOption
      res2 = RefToEntityWithVersion.getOnlyLatestVersions(
        res.allUserRefs
      )
      res3 = res2.map(helper.ref2EntityOption(_))
//      res4 = res3.map(x => x.optionEntity)
      res5 = helper.getEntityListAsVDOM(res3, optEntity2VDOM)
    } yield (res5)

  }

}
