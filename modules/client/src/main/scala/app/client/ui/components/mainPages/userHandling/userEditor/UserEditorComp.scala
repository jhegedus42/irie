package app.client.ui.components.mainPages.userHandling.userEditor

import app.client.ui.caching.cache.{
  CacheConvenienceFunctions,
  CacheEntryStates
}
import app.client.ui.caching.cacheInjector.{
  Cache,
  CacheAndProps,
  MainPageReactCompWrapper,
  ToBeWrappedMainPageComponent
}
import app.client.ui.components.mainPages.LoginPageComp.{Props, State}
import app.client.ui.components.mainPages.userHandling.userEditor.UserEditorComp.{
  Props,
  UserEditorPage
}
import app.client.ui.components.{MainPage, MainPageWithCache}
import app.shared.comm.postRequests.{GetEntityReq, UpdateReq}
import app.shared.entity.Entity
import app.shared.entity.entityValue.values.User
import app.shared.utils.UUID_Utils.EntityIdentity
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import japgolly.scalajs.react.{
  BackendScope,
  Callback,
  CallbackTo,
  CtorType,
  ScalaComponent
}
import org.scalajs.dom
import org.scalajs.dom.html.Div

trait UserEditorComp
    extends ToBeWrappedMainPageComponent[
      UserEditorComp,
      UserEditorPage
    ] {

  override type Props = UserEditorComp.Props
  override type Backend =
    UserEditorComp.Backend[UserEditorComp.Props]
  override type State = UserEditorComp.State

}

object UserEditorComp {

  case class UserEditorPage(paramFromURL: String)
      extends MainPageWithCache[UserEditorComp, UserEditorPage]

  case class UpdatedUser(optEnt: Option[Entity[User]])
  case class State(updatedUser:  UpdatedUser)

  case class Props(
    userIdentity: EntityIdentity,
    routerCtl:    RouterCtl[MainPage])

  val component: Component[
    CacheAndProps[Props],
    State,
    Backend[Props],
    CtorType.Props
  ] = {
    ScalaComponent
      .builder[CacheAndProps[Props]](
        "This is a userEditor page. It demonstrates all crucial functionality."
      )
      .initialState(State(UpdatedUser(None)))
      .renderBackend[Backend[Props]]
      .build
  }

  object Helpers {

    def nameField(
      optEnt:        Option[Entity[User]],
      updateHandler: CallbackTo[Unit]
    ) = {

      def g[V](v: Option[V])(f: V => VdomTagOf[Div]): VdomTagOf[Div] =
        if (optEnt.isEmpty) <.div(<.p("loading ..."))
        else f(v.get)

      g(optEnt) { e =>
        val name = e.entityValue.name
        <.div(
          <.br,
          "Name: ",
          TextField.textFieldComp(name)(
            TextField.Props(updateHandler)
          )
        )
      }

    }

  }

  class Backend[Properties](
    $ : BackendScope[CacheAndProps[Properties], State]) {

    def render(
      cacheAndProps: CacheAndProps[Props],
      s:             State
    ): VdomElement = {

      val ent: Option[Entity[User]] =
        CacheConvenienceFunctions.getEntity[User](
          cacheAndProps.props.userIdentity,
          cacheAndProps.cache
        )

      import org.scalajs.dom.html.{Anchor, Div}

      def updateUserName(
        currentEntity: Entity[User],
        newName:       String
      ): Option[Entity[User]] = {

        import monocle.macros.syntax.lens._

        val newEntityVal =
          currentEntity.entityValue.lens(_.name).set(newName)

        val par: UpdateReq[User]#ParT =
          UpdateReq.UpdateReqPar[User](currentEntity, newEntityVal)

        val res: CacheEntryStates.CacheEntryState[UpdateReq[User]] =
          cacheAndProps.cache
            .getResultOfCachedPostRequest[UpdateReq[User]](par)(???,
                                                                ???,
                                                                ???,
                                                                ???,
                                                                ???)

        val res2: Option[Entity[User]] =
          res.toOption.map(x => x.entity)
        res2
      }

      def handleUpdateUserButon(): CallbackTo[Unit] = {
        Callback({

          dom.window.alert(
            s"mi ezt az usert-t fogjuk update-elni : $ent"
          )

        })

        // call update user request
      }


      <.div(
        <.h1("This is the UserEditor Page"),
        <.br,
        Helpers.nameField(ent,handleUpdateUserButon()),
      )

    }

  }

}
//
//   when the update returns, it simply triggers a re-render
//   and at that point the cache launches a get entity request
//   to make its stale entity fresh again
//
//   so it can go like this - as well (I think I prefer this way):
//
//   up-to-date => update-request-sent => update-request-returned =>
//   getEntity-AKA-refresh-request-sent => getEntity-returned-entry-is-
//   refreshed (not-stale-any-longer)
//
//
