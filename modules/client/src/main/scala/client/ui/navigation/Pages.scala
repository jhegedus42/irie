package client.ui.navigation

import bootstrap4.TB.C
import client.ui.compositeWidgets.specific.note.{
  NoteFolderUpdaterWidget,
  NotesWidget
}

import scala.concurrent.ExecutionContextExecutor
import bootstrap4.TB.C
import client.cache.Cache
import client.cache.relationalOperations.CellOptionMonad.CellOption
import client.ui.compositeWidgets.general.{
  EntityCreatorWidget,
  EntitySelectorWidget,
  TextFieldUpdaterWidget
}
import client.ui.compositeWidgets.specific.{
  SaveDataOnServerButton,
  UserAdminWidget
}
import client.ui.compositeWidgets.specific.image.{
  ImageDisplayerWidget,
  ImageUploaderWidget,
  ImagesForANote
}
import client.ui.compositeWidgets.specific.image.svg.{
  CompositeSVGDisplayer,
  VisualLinkAsSVGHelpers
}
import client.ui.compositeWidgets.specific.note.NotesWidget
import client.ui.compositeWidgets.specific.noteFolder.ImageSequenceTraversingWidget
import client.ui.navigation.{NavigatorComp, Pages}
import client.ui.wrappedReact.{
  Crop,
  ImgCropWidget,
  ReactCrop,
  TagsInput
}
import japgolly.scalajs.react.vdom.SvgTagOf
import shared.dataStorage.model.{
  CanProvideDefaultValue,
  HintForNote,
  ImgFileName,
  Note,
  SizeInPercentage
}
import japgolly.scalajs.react.{CtorType, ScalaComponent}
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement}
import org.scalajs.dom.html.Div
import client.cache.relationalOperations.RelationalOperations.Pipe
import client.sodium.core
import client.ui.atomicWidgets.show.HiderWidget
import client.ui.compositeWidgets.specific.image.rect.VisualHintEditor
import client.ui.navigation.Pages.noteCreator
import japgolly.scalajs.react.component.Scala.Component
import shared.dataStorage.relationalWrappers.{
  Ref,
  TypedReferencedValue
}

case class Page(
  name:      String,
  vdomTagOf: VdomTagOf[Div])

object Pages {

  implicit val noteCache: Cache[Note] = Cache.noteCache

  val selector: EntitySelectorWidget[Note] =
    EntitySelectorWidget[Note]({ x: Note =>
      {
        val imgFileName =
          x.visualHint.hint.fileName.fileNameWithPathAsString
        <.div(x.title,
              <.br,
              <.img(^.src := s"$imgFileName",
                    ^.width := "200px",
                    ^.alt := "image"),
              <.br,
              <.hr)

      }
    })

  val selectedNoteRefSetterStream: core.Stream[Option[Ref[Note]]] =
    selector.selectedEntityRefInjector

  val imageSequenceTraversingWidget =
    ImageSequenceTraversingWidget(selectedNoteRefSetterStream)

  val selectedNote: CellOption[TypedReferencedValue[Note]] =
    imageSequenceTraversingWidget.resolvedNote

  val pagesAfterLogin = List(imgSeq,
                             noteSelector,
                             noteCreator,
                             noteTextEditor,
                             imageUploader,
                             visualLinkEditorForSelectedNote,
                             noteFolderEditor,
                             backupDataOnServer,
                             userAdminPage)

  lazy val imgSeq = Page(
    "Note Seq", {
      <.div(
        imageSequenceTraversingWidget.vdom
      )
    }
  )

  lazy val noteSelector = Page(
    "Note Selector", {
      <.div(selector.selectorTable.comp())
    }
  )

  lazy val noteCreator = {
    val noteCreator = EntityCreatorWidget({ () =>
      CanProvideDefaultValue.defValOf[Note]
    }, "Note")
    Page("Note Creator", {
      <.div(
        noteCreator.createNewEntityButton.comp()
      )
    })
  }

  lazy val noteTextEditor = {

    val noteTitleEditor = TextFieldUpdaterWidget[Note](
      "title",
      selector.selectedEntityResolved,
      noteCache, { n: Note =>
        n.title
      }, { (n: Note, s: String) =>
        n.copy(title = s)
      }
    )

    Page(
      "Note Text Editor", {
        <.div(
          noteTitleEditor.comp()
        )
      }
    )
  }

  lazy val visualLinkEditorForSelectedNote = {

    val imgQueEditor = {
      import monocle.macros.syntax.lens._
      VisualHintEditor(
        selectedNote
      )
    }

//    lazy val selectedVisualHint: CellOption[HintForNote] =
//      selectedNote.map(_.versionedEntityValue.valueWithoutVersion.visualHint)

    val visualLinkDisplayer = CompositeSVGDisplayer(selectedNote).visualLinkAsVDOM

    Page(
      "Visual Link Editor", {
        <.div(
          <.h3("Visual Link Editor"),
          visualLinkDisplayer,
          <.br,
          imageSequenceTraversingWidget.goToNextNoteButton(),
          <.br,
          imgQueEditor.getComp()
        )
      }
    )
  }

  lazy val imageUploader = {

    val imageUploaderWidget = ImageUploaderWidget(selectedNote)
    val imageDisplayerWidget = ImageDisplayerWidget(
      selectedNote.map(_.versionedEntityValue.valueWithoutVersion)
    )

    Page(
      "Image Uploader", {
        <.div(
          imageDisplayerWidget.imageDisplayer(),
          imageUploaderWidget.comp.optDisplayer()
        )
      }
    )

  }

  lazy val noteFolderEditor = {
    val noteFolderUpdater = NoteFolderUpdaterWidget(
      selector.selectedEntityResolved
    )

    val noteFolderUpdaterComp
      : Component[Unit, Unit, Unit, CtorType.Nullary] =
      noteFolderUpdater.getComp

    Page("Note Folder Editor", {
      <.div(
        noteFolderUpdaterComp()
      )
    })
  }

  lazy val userAdminPage = {
    val page = UserAdminWidget()
    Page("User Admin Page",
         <.div(
           page.getComp()
         ))
  }

  lazy val backupDataOnServer = Page(
    "Server Admin", {
      val saveDataOnServerButton = SaveDataOnServerButton()
      <.div(
        <.main(C.container, ^.role := "container")(
          <.div(C.jumbotron)(
            saveDataOnServerButton.vdom
          )
        )
      )
    }
  )

}
