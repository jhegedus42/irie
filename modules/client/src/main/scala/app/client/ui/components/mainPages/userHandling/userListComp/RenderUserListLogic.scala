package app.client.ui.components.mainPages.userHandling.userListComp

import app.client.ui.caching.cache.CacheEntryStates
import app.client.ui.caching.cacheInjector.{Cache, CacheAndProps, MainPageReactCompWrapper, ToBeWrappedMainPageComponent}
import app.client.ui.components.mainPages.userHandling.UserEditorComp.UserEditorPage
import app.client.ui.components.mainPages.userHandling.userListComp.UserListComp.Props
import app.client.ui.components.{MainPage, StaticTemplatePage, UserListPage}
import app.shared.comm.postRequests.{AdminPassword, GetAllUsersReq, GetEntityReq}
import app.shared.entity.Entity
import app.shared.entity.entityValue.values.User
import app.shared.entity.refs.RefToEntityWithoutVersion
import app.shared.utils.UUID_Utils.EntityIdentity
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^.{<, ^, _}
import japgolly.scalajs.react.vdom.{VdomElement, html_<^}
import japgolly.scalajs.react.{BackendScope, CtorType, ScalaComponent}
import org.scalajs.dom.html.{Anchor, Div}

case class RenderUserListLogic(
  cacheInterfaceWrapper: CacheAndProps[Props]) {

  def userListAsVDOM: Option[html_<^.TagMod] = {

    def refToAllUsersOption: Option[GetAllUsersReq.Res] = {
      def requestResultForRefToAllUsers
        : CacheEntryStates.CacheEntryState[GetAllUsersReq] =
        cacheInterfaceWrapper.cache
          .getPostReqResult[GetAllUsersReq](
            GetAllUsersReq.Par(AdminPassword("titok"))
          )
      requestResultForRefToAllUsers.toOption
    }

    def getUserListAsVDOM(l: List[Option[Entity[User]]]): TagMod = {

      /**
        * todo-now - tenni ide egy linket
        * @param user
        * @return
        */
      def optUserEntity2VDOM(
        user: Option[Entity[User]]
      ): VdomElement = {
        val ctl = cacheInterfaceWrapper.props.routerCtl

        def linkToUserEditorPage(
          id: EntityIdentity
        ): VdomTagOf[Anchor] =
            <.a("edit", ^.href := ctl.urlFor(StaticTemplatePage).value,
            ctl.setOnLinkClick(UserEditorPage(id.uuid)))

        if (user.isDefined) {
          <.p(
            user.get.entityValue.name," ",
            linkToUserEditorPage(user.get.refToEntity.entityIdentity),
            <.br
          )
        } else <.div("user info not loaded yet ...")
      }

      TagMod(l.map(optUserEntity2VDOM(_)).toVdomArray)
    }

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

    for {
      res <- refToAllUsersOption
      res2 = res.allUserRefs
      res3 = res2.map(userRef2UserOption(_))
      res4 = res3.map(x => x.optionEntity)
      res5 = getUserListAsVDOM(res4)
    } yield (res5)
  }

}
