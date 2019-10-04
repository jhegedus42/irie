package app.client.ui.components.mainPages.userHandling.userEditor

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
import app.shared.entity.Entity
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
    resultOfUserUpdateRequest: Option[Entity[User]])

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
                  counter: Int,
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
      .initialState(State(counter=0,updatedUser=UpdatedUser(None)))
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

    /**
      *
      * If the option is None, this displays some "default" text,
      * otherwise, it displays the
      * @param entityOption
      * @param updateHandler
      * @return
      */
    def intendedNewNameTextField(
      entityOption:  Option[Entity[User]],
      updateHandler: String => CallbackTo[Unit]
    ) = {

      def g[V](v: Option[V])(f: V => VdomTagOf[Div]): VdomTagOf[Div] =
        if (entityOption.isEmpty) <.div(<.p("loading ..."))
        else f(v.get)

      val res: html_<^.VdomTagOf[Div] = g(entityOption) {
        userEntity: Entity[User] =>
          val userName = userEntity.entityValue.name
          <.div(
            <.br,
            s"Original name: $userName",
            <.br,
            <.hr,
            <.br,
            TBH.textFieldComp(userName)(
              TBH.Props(
                updateHandler,
                "Save Changes",
                "Intended new name for the user (TextField implementation): "
              )
            )
          )
      }

      res

    }

    def updateUserName(
      currentEntity: Entity[User],
      newName:       String,
      cacheAndProps: CacheAndProps[Props]
    ): Option[Entity[User]] = {

      val newEntityVal =
        currentEntity.entityValue.lens(_.name).set(newName)

      val par: UpdateReq[User]#ParT =
        UpdateReq.UpdateReqPar[User](currentEntity, newEntityVal)

      // todo-now 1.2 complete this

      // todo-now 1.3 kell egy kess ami elerheto

      // todo-now 1.4 send update request

      None
    }

    def handleUpdateUserButon(
      message: String
    ): String => CallbackTo[Unit] = { newValue: String =>
      Callback({

        dom.window.alert(
          s"mi ezt az usert-t fogjuk update-elni : $message\n" +
            s"ez lesz az uj neve: $newValue"
        )

      })

    // call update user request
      // todo-now-1.1 handle button press
    }

  }

  class Backend[Properties](
    $ : BackendScope[CacheAndProps[Props], State]) {

    def render(
      cacheAndProps: CacheAndProps[Props],
      s:             State
    ): VdomElement = {

      val entityOption: Option[Entity[User]] =
        CacheConvenienceFunctions.getEntity[User](
          cacheAndProps.props.userIdentity,
          cacheAndProps.cache
        )

      import org.scalajs.dom.html.{Anchor, Div}


      <.div(
        <.h1("This is the UserEditor Page"),
        <.br,
        "Current value of the User "+entityOption,
        <.br,
        "Intended new name for the user (UserEditorComp implementation): ",
        <.input.text(
          ^.onChange.==>(Helpers.onChangeIntendedNewName($)),
          ^.value.:=(s.intendedNewName.value.getOrElse("None"))
        )

        // todo-now continue-here
        // todo-now 1 - send update user ajax call button

        // todo-now-2  button to increase counter => refresh page

        // todo-now-3  button to trigger cache refresh
      )

    }

  }

}
