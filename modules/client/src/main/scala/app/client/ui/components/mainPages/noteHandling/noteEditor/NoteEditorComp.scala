package app.client.ui.components.mainPages.NoteHandling.NoteEditor

import app.client.ui.caching.cache.comm.write.WriteRequestHandlerTCImpl
import app.client.ui.caching.cache.{
  CacheHelperFunctions,
  ReadCacheEntryStates
}

import app.client.ui.caching.cacheInjector.{
  Cache,
  CacheAndPropsAndRouterCtrl,
  MainPageReactCompWrapper,
  ToBeWrappedMainPageComponent
}

import app.client.ui.components.mainPages.login.LoginPageComp.{
  Props,
  State
}
import app.client.ui.components.mainPages.NoteHandling.NoteEditor.NoteEditorComp.{
  Props,
  NoteEditorPage
}
import app.client.ui.components.{
  MainPage,
  MainPageInjectedWithCacheAndController
}
import app.shared.comm.WriteRequest
import app.shared.comm.postRequests.{GetEntityReq, UpdateReq}
import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.values.Note
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


trait NoteEditorComp
    extends ToBeWrappedMainPageComponent[
      NoteEditorComp,
      NoteEditorPage
    ] {

  /**
    *
    * We use the T at the end to indicate that this is
    * an abstract type to be overriden.
    *
    */
  override type PropsT = NoteEditorComp.Props
//  override type BackendT =
//    NoteEditorComp.Backend[NoteEditorComp.Props]
  override type StateT = NoteEditorComp.State

}

object NoteEditorComp {

  case class NoteEditorPage(paramFromURL: String)
      extends MainPageInjectedWithCacheAndController[NoteEditorComp,
                                                     NoteEditorPage]

  case class UpdatedNote(
    resultOfNoteUpdateRequest: Option[EntityWithRef[Note]])

  /**
    *
    * This should be used by the textfield
    *
    * @param value this is the name to which the Note's name
    *              should change after the update request has
    *              completed (so this stores our "intention")
    */
  case class IntendedNewName(value: Option[String] = None)

  /**
    * @param updatedNote
    * @param intendedNewName
    */
  case class State(
    counter:         Int,
    updatedNote:     UpdatedNote,
    intendedNewName: IntendedNewName = IntendedNewName())

  case class Props( NoteIdentity: EntityIdentity[Note] )

  val component: Component[
    CacheAndPropsAndRouterCtrl[Props],
    State,
    Backend[Props],
    CtorType.Props
  ] = {
    ScalaComponent
      .builder[CacheAndPropsAndRouterCtrl[Props]](
        "This is a NoteEditor page. It demonstrates all crucial functionality."
      )
      .initialState(
        State(counter = 0, updatedNote = UpdatedNote(None))
      )
      .renderBackend[Backend[Props]]
      .build
  }

  object Helpers {
    import app.client.ui.caching.cacheInjector.CacheAndPropsAndRouterCtrl
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
      bs: BackendScope[CacheAndPropsAndRouterCtrl[Props], State]
    )(e:  ReactEventFromInput
    ): CallbackTo[Unit] = {
      val event:  ReactEventFromInput = e
      val target: Input               = event.target
      val text:   String              = target.value
      bs.modState(
        s => s.lens(_.intendedNewName.value).set(Some(text))
      )
    }

//    def handleUpdateNoteButon(
//      cache:            Cache,
//      currentEntityPar: EntityWithRef[Note]
//    ): String => CallbackTo[Unit] = { newNoteName: String =>
//      Callback({
//
//        implicit val i: WriteRequestHandlerTCImpl[WriteRequest,
//                                                  UpdateReq[Note]]
//          with WriteRequestHandlerTCImpl.UpdateReqNoteCacheInvalidator =
//          WriteRequestHandlerTCImpl.NoteUpdater
//
//        import io.circe.generic.auto._
//
//        val newEntityVal =
//          currentEntityPar.entityValue.lens(_.name).set(newNoteName)
//
//        val ur1 =
//          UpdateReq.UpdateReqPar[Note](currentEntityPar, newEntityVal)
//
//        cache.writeToServer[WriteRequest, UpdateReq[Note]](ur1)
//
//      })
//    }
  }

  class Backend[Properties](
    $ : BackendScope[CacheAndPropsAndRouterCtrl[Props], State]) {

    def render(
      cacheAndProps: CacheAndPropsAndRouterCtrl[Props],
      s:             State
    ): VdomElement = {

      println(
        "render was called in" +
        " Note Editor Comp - 7841813D-31B2-48F2-9481-A1240013FBEB"
      )

      def entityOption: Option[EntityWithRef[Note]] =
        CacheHelperFunctions.getEntity[Note](
          cacheAndProps.props.NoteIdentity,
          cacheAndProps.cache
        )

      import org.scalajs.dom.html.{Anchor, Div}
      def newName: String =
        s.intendedNewName.value
          .getOrElse("there is no new name defined yet")

      def currentName: String =
        entityOption.map(_.entityValue.name).getOrElse("Loading...")
//
//      def buttonOpt: VdomTagOf[Div] =
//        if (entityOption.nonEmpty) {
//          <.div(
//            Helpers.saveButton(
//              Helpers.handleUpdateNoteButon(cacheAndProps.cache,
//                                            entityOption.get)(newName)
//            )
//          )
//
//        } else <.div("  No entity, no button !")
//
      <.div(
        <.h1("This is the NoteEditor Page"),
        <.br,
        "Current name of the Note " + currentName,
        <.br,
//        s"our entity option is :",
//        <.br,
//        s"$entityOption",
        <.br,
        "Intended new name for the Note (NoteEditorComp implementation): ",
        <.input.text(
          ^.onChange.==>(Helpers.onChangeIntendedNewName($)),
          ^.value.:=(s.intendedNewName.value.getOrElse("None"))
        ),
        <.br,
        buttonOpt,
        <.br
      )

    }

  }

}
