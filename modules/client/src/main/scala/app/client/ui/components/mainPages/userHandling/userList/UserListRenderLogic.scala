package app.client.ui.components.mainPages.userHandling.userList

import app.client.ui.caching.cache.ReadCacheEntryStates
import app.client.ui.caching.cacheInjector.{Cache, CacheAndPropsAndRouterCtrl, MainPageReactCompWrapper, ToBeWrappedMainPageComponent}
import app.client.ui.components.mainPages.userHandling.userEditor.UserEditorComp.UserEditorPage
import app.client.ui.components.mainPages.userHandling.userList.UserListComp.Props
import app.client.ui.components.{MainPage, StaticTemplatePage, UserListPage}
import app.shared.comm.ReadRequest
import app.shared.comm.postRequests.read.{AdminPassword, GetAllUsersReq}
import app.shared.comm.postRequests.{GetEntityReq}
import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.values.User
import app.shared.entity.refs.RefToEntityWithVersion
import app.shared.utils.UUID_Utils.EntityIdentity
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^.{<, ^, _}
import japgolly.scalajs.react.vdom.{VdomElement, html_<^}
import japgolly.scalajs.react.{BackendScope, CtorType, ScalaComponent}
import org.scalajs.dom.html.{Anchor, Div}

case class UserListRenderLogic(
  cacheInterfaceWrapper: CacheAndPropsAndRouterCtrl[Props]) {

  def userListAsVDOM: Option[html_<^.TagMod] = {

    def refToAllUsersOption: Option[GetAllUsersReq.Res] = {
      def requestResultForRefToAllUsers
        : ReadCacheEntryStates.ReadCacheEntryState[ReadRequest,
                                                   GetAllUsersReq] =
        cacheInterfaceWrapper.cache
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
        val ctl = cacheInterfaceWrapper.props.routerCtl

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
        cacheInterfaceWrapper.cache
          .readFromServer[ReadRequest, GetEntityReq[User]](par_)
          .toOption
      val emptyResult: GetEntityReq.Res[User] =
        GetEntityReq.Res[User](None)

      res_.getOrElse(emptyResult)
    }

    for {
      res <- refToAllUsersOption
      res2  = RefToEntityWithVersion.getOnlyLatestVersions(res.allUserRefs)
      res3  = res2.map(userRef2UserOption(_))
      res4  = res3.map(x => x.optionEntity)
      res5  = getUserListAsVDOM(res4)
    } yield (res5)

  }

}
