package app.client.ui.components.mainPages.userHandling.userEditor

import app.client.ui.caching.cache.comm.write.WriteRequestHandlerTCImpl
import app.client.ui.caching.cache.{
  CacheConvenienceFunctions,
  ReadCacheEntryStates
}

import app.client.ui.caching.cacheInjector.{
  Cache,
  CacheAndProps,
  MainPageReactCompWrapper,
  ToBeWrappedMainPageComponent
}

import app.client.ui.components.mainPages.LoginPageComp.{Props, State}
import app.client.ui.components.mainPages.userHandling.userEditor.{
  TextFieldWithButtonAndHandler => TBH
}
import app.client.ui.components.mainPages.userHandling.userEditor.UserEditorComp.{
  Props,
  UserEditorPage
}
import app.client.ui.components.{MainPage, MainPageWithCache}
import app.shared.comm.WriteRequest
import app.shared.comm.postRequests.{GetEntityReq, UpdateReq}
import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.values.User
import app.shared.utils.UUID_Utils.EntityIdentity
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.{VdomElement, html_<^}
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import japgolly.scalajs.react.{
  BackendScope,
  Callback,
  CallbackTo,
  CtorType,
  ScalaComponent
}
import monocle.macros.syntax.lens._
import org.scalajs.dom
import org.scalajs.dom.html.{Button, Div, Input}

trait UserEditorComp
    extends ToBeWrappedMainPageComponent[
      UserEditorComp,
      UserEditorPage
    ] {

  /**
    *
    * We use the T at the end to indicate that this is
    * an abstract type to be overriden.
    *
    */
  override type PropsT = UserEditorComp.Props
  override type BackendT =
    UserEditorComp.Backend[UserEditorComp.Props]
  override type StateT = UserEditorComp.State

}

object UserEditorComp {

  case class UserEditorPage(paramFromURL: String)
      extends MainPageWithCache[UserEditorComp, UserEditorPage]

  case class UpdatedUser(
    resultOfUserUpdateRequest: Option[EntityWithRef[User]])

  /**
    *
    * This should be used by the textfield
    *
    * @param value this is the name to which the User's name
    *              should change after the update request has
    *              completed (so this stores our "intention")
    */
  case class IntendedNewName(value: Option[String] = None)

  /**
    * @param updatedUser
    * @param intendedNewName
    */
  case class State(
    counter:         Int,
    updatedUser:     UpdatedUser,
    intendedNewName: IntendedNewName = IntendedNewName())

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
      .initialState(
        State(counter = 0, updatedUser = UpdatedUser(None))
      )
      .renderBackend[Backend[Props]]
      .build
  }

  object Helpers {
    import app.client.ui.caching.cacheInjector.CacheAndProps
    import japgolly.scalajs.react.vdom.html_<^.{
      <,
      TagMod,
      VdomElement,
      ^,
      _
    }
    import japgolly.scalajs.react.{
      BackendScope,
      Callback,
      CallbackTo,
      CtorType,
      ReactEventFromInput,
      ScalaComponent
    }
    import monocle.macros.syntax.lens._
    import org.scalajs.dom
    import org.scalajs.dom.html.{Button, Input}

    def saveButton(handler: CallbackTo[Unit]): VdomTagOf[Button] = {
      import bootstrap4.TB.convertableToTagOfExtensionMethods

      <.button.btn.btnPrimary(
        "Save changes.",
        ^.onClick --> handler
      )
    }

    def onChangeIntendedNewName(
      bs: BackendScope[CacheAndProps[Props], State]
    )(e:  ReactEventFromInput
    ): CallbackTo[Unit] = {
      val event:  ReactEventFromInput = e
      val target: Input               = event.target
      val text:   String              = target.value
      bs.modState(
        s => s.lens(_.intendedNewName.value).set(Some(text))
      )
    }

    def handleUpdateUserButon(
      cache:            Cache,
      currentEntityPar: EntityWithRef[User]
    ): String => CallbackTo[Unit] = { newUserName: String =>
      Callback({
//        dom.window.alert(
//          s"mi ezt az usert-t fogjuk update-elni : ${currentEntityPar.entityValue.name}\n" +
//            s"ez lesz az uj neve: $newUserName"
//        )

        implicit val i
          : WriteRequestHandlerTCImpl[WriteRequest, UpdateReq[User]]
            with WriteRequestHandlerTCImpl.UserReadCacheInvalidator =
          WriteRequestHandlerTCImpl.userUpdater

        import io.circe.generic.auto._

        val newEntityVal =
          currentEntityPar.entityValue.lens(_.name).set(newUserName)

        val ur1 =
          UpdateReq.UpdateReqPar[User](currentEntityPar, newEntityVal)

        cache.writeToServer[WriteRequest, UpdateReq[User]](ur1)

      })
    }
  }

  class Backend[Properties](
    $ : BackendScope[CacheAndProps[Props], State]) {

    def render(
      cacheAndProps: CacheAndProps[Props],
      s:             State
    ): VdomElement = {

      def entityOption: Option[EntityWithRef[User]] =
        CacheConvenienceFunctions.getEntity[User](
          cacheAndProps.props.userIdentity,
          cacheAndProps.cache
        )

      import org.scalajs.dom.html.{Anchor, Div}
      def newName: String =
        s.intendedNewName.value
          .getOrElse("there is no new name defined yet")

      def currentName: String =
        entityOption.map(_.entityValue.name).getOrElse("Loading...")

      def buttonOpt: VdomTagOf[Div] =
        if (entityOption.nonEmpty) {
          <.div(
            Helpers.saveButton(
              Helpers.handleUpdateUserButon(cacheAndProps.cache,
                                            entityOption.get)(newName)
            )
          )

        } else <.div("  No entity, no button !")

      <.div(
        <.h1("This is the UserEditor Page"),
        <.br,
        "Current name of the User " + currentName,
        <.br,
        "Intended new name for the user (UserEditorComp implementation): ",
        <.input.text(
          ^.onChange.==>(Helpers.onChangeIntendedNewName($)),
          ^.value.:=(s.intendedNewName.value.getOrElse("None"))
        ),
        <.br,
        buttonOpt,
        <.br
      )

      // todo-now-2 - button to increase counter => refresh page

      // todo-right-now

      // todo-now-3 - button to trigger cache refresh

    }

  }

}
