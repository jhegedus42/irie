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
import client.ui.atomicWidgets.show.HiderWidget
import client.ui.compositeWidgets.specific.image.rect.VisualHintEditor
import client.ui.navigation.Pages.noteCreator
import japgolly.scalajs.react.component.Scala.Component
import shared.dataStorage.relationalWrappers.TypedReferencedValue

case class Page(
  name:      String,
  vdomTagOf: VdomTagOf[Div])

object Pages {

  implicit lazy val noteCache: Cache[Note] = Cache.noteCache
  lazy val nw = NotesWidget()

  lazy val selectedNote: CellOption[TypedReferencedValue[Note]] =
    selector.selectedEntityResolved |> CellOption.fromCellOption

  lazy val imageSequenceTraversingWidget =
    ImageSequenceTraversingWidget()

  lazy val selector: EntitySelectorWidget[Note] =
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

  lazy val pages = List(imgSeq,
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
    lazy val noteCreator = EntityCreatorWidget({ () =>
      CanProvideDefaultValue.defValOf[Note]
    }, "Note")
    Page("Note Creator", {
      <.div(
        noteCreator.createNewEntityButton.comp()
      )
    })
  }

  lazy val noteTextEditor = {

    lazy val noteTitleEditor = TextFieldUpdaterWidget[Note](
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

    lazy val imgQueEditor = {
      import monocle.macros.syntax.lens._
      VisualHintEditor(
        selectedNote
      )
    }

//    lazy val selectedVisualHint: CellOption[HintForNote] =
//      selectedNote.map(_.versionedEntityValue.valueWithoutVersion.visualHint)

    lazy val visualLinkDisplayer = CompositeSVGDisplayer(selectedNote).visualLinkAsVDOM

    Page(
      "Visual Link Editor", {
        <.div(
          <.h3("Visual Link Editor"),
          visualLinkDisplayer,
          <.br,
          imgQueEditor.getComp()
        )
      }
    )
  }

  lazy val imageUploader = {
    lazy val imageUploaderWidget = ImageUploaderWidget(selectedNote)
    lazy val visualLinkDisplayer = CompositeSVGDisplayer(selectedNote).visualLinkAsVDOM
    Page(
      "Image Uploader", {
        <.div(
          visualLinkDisplayer,
          imageUploaderWidget.comp.optDisplayer()
        )
      }
    )

  }

  lazy val noteFolderEditor = {
    val noteFolderUpdater = NoteFolderUpdaterWidget(
      selector.selectedEntityResolved
    )

    lazy val noteFolderUpdaterComp
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
      lazy val saveDataOnServerButton = SaveDataOnServerButton()
      <.div(
        <.main(C.container, ^.role := "container")(
          <.div(C.jumbotron)(
            saveDataOnServerButton.btn.comp()
          )
        )
      )
    }
  )

}
