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
      .initialState(State(UpdatedUser(None)))
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

//      val res
//        : CacheEntryStates.CacheEntryState[WriteRequest, UpdateReq[
//          User
//        ]] =
//        cacheAndProps.cache
//          .getResultOfCachedPostRequest[WriteRequest, UpdateReq[
//            User
//          ]](par)(???, ???, ???, ???, ???)

//      val res2: Option[Entity[User]] =
//        res.toOption.map(x => x.entity)

//      res2

      ???
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

      //
      //
      // react props:
      //    - user identity
      //    - router controller
      //
      //
      // current state of cache displayed in a textfield
      //    - (raw/crud/unprocessed), simply display whatever
      //       the cache returns, as it is, usint a .toString
      //
      //    -  for this, we use "get entity from server"
      //       request
      //
      //
      //
      //
      // intended new name                      - tikk
      //    - state of type : `IntendedNewName`
      //    - textfield for editing showing and editing
      //      this state
      //
      //
      //
      // refresh button (increase counter) :
      //
      //    - react state `SCounter` of type `Int`
      //
      //    - button handler that increases this state
      //      by one when pressed (this will trigger
      //      a re-render, hopefully)
      //
      //    - a field showing the value of `S_Counter`
      //
      //
      //
      // invalidate cache button
      //  - invalidate cache function
      //       - in placed into the cache object
      //       - sets type of entry to `Stale`
      //         for the given (Read) Request
      //
      //         (it makes no sense to have such
      //          `Stale` state for write request
      //          "caches", and it is not even clear
      //          if it makes sense to have any type
      //          "cache" for write requests - in
      //          the first place, because their result
      //          is not meant to be displayed, they
      //          only exist for creating side effects)
      //
      //
      //  - cache has entry type of `Stale`
      //
      //
      //
      //
      //
      // refresh kess button
      //
      //
      //
      // react state `S_UpdateRequest` :
      //          - have we pressed the update
      //            button already ? YES/NO
      //
      //          - the starting (initial) value
      //            of this state is NO
      //
      //
      // field showing `S_UpdateRequest`
      //
      //
      // if field `S_UpdateRequest` is true
      //   then show the result of the update request
      //     which can be
      //       1) pending
      //       2) ready with
      //           2.1) success
      //           2.2) OCC failure
      //
      //
      // button to set S1 from false to true
      //    - button
      //    - handler

      <.div(
        <.h1("This is the UserEditor Page"),
        <.br,
        "Intended new name for the user (UserEditorComp implementation): ",
        <.input.text(
          ^.onChange.==>(Helpers.onChangeIntendedNewName($)),
          ^.value.:=(s.intendedNewName.value.getOrElse("None"))
        )
      )

    }

  }

}
