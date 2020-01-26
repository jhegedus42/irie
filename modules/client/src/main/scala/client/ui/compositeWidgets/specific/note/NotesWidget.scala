package client.ui.compositeWidgets.specific.note

import client.cache.relationalOperations.CellOptionMonad.CellOption
import client.cache.relationalOperations.RelationalOperations.ResultSet
import client.cache.relationalOperations.onDataModel.NoteOperations
import client.cache.{Cache, CacheMap}
import client.sodium.core.{CellLoop, CellSink}
import client.ui.atomicWidgets.input.SButton
import client.ui.atomicWidgets.show.HiderWidget
import client.ui.atomicWidgets.show.text.SWPreformattedText
import client.ui.atomicWidgets.templates.CellTemplate
import client.ui.compositeWidgets.general.{CellOptionDisplayerWidget, CellOptionListWidget, EntityCreatorWidget, EntitySelectorWidget, TextFieldUpdaterWidget}
import client.ui.compositeWidgets.specific.image.ImagesForANote
import client.ui.compositeWidgets.specific.image.svg.CompositeSVGDisplayer
import client.ui.helpers.table.TableHelpers
import japgolly.scalajs.react.{CtorType, ScalaComponent}
import japgolly.scalajs.react.component.Scala.{Component, Unmounted}
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}
import org.scalajs.dom.html.Div
import shared.dataStorage.model.{CanProvideDefaultValue, HintForNote, Note}
import shared.dataStorage.relationalWrappers.TypedReferencedValue

import scala.concurrent.ExecutionContextExecutor

case class NotesWidget() {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  implicit lazy val noteCache: Cache[Note] = Cache.noteCache

  lazy val selector: EntitySelectorWidget[Note] =
    EntitySelectorWidget[Note]({ x: Note =>
      x.title
    })

  import client.cache.relationalOperations.RelationalOperations.Pipe

  lazy val selectedNote: CellOption[TypedReferencedValue[Note]] =
    selector.selectedEntityResolved |> CellOption.fromCellOption

  lazy val noteTitleEditor = TextFieldUpdaterWidget[Note](
    "title",
    selector.selectedEntityResolved,
    noteCache, { n: Note =>
      n.title
    }, { (n: Note, s: String) =>
      n.copy(title = s)
    }
  )

  val noteFolderUpdater = NoteFolderUpdaterWidget(
    selector.selectedEntityResolved
  )

  lazy val noteCreator = EntityCreatorWidget({ () =>
    CanProvideDefaultValue.defValOf[Note]
  }, "Note")

  lazy val noteFolderUpdaterComp
    : Component[Unit, Unit, Unit, CtorType.Nullary] =
    noteFolderUpdater.getComp

  lazy val imagesForANoteComp
    : Component[Unit, Unit, Unit, CtorType.Nullary] = ImagesForANote(
    selectedNote
  ).getComp

  lazy val imagesForANoteWithHider =
    HiderWidget("Images", imagesForANoteComp).hider

  lazy val visualLinkDisplayer = CompositeSVGDisplayer(selectedNote).visualLinkAsVDOM

  def getComp = {

    def render: Unit => VdomElement = { _ =>
      <.div(
        <.hr,
        <.h2("Notes"),
        <.br,
        selector.selectorTable.comp(),
        visualLinkDisplayer,
        noteCreator.createNewEntityButton.comp(),
        noteTitleEditor.comp(),
        imagesForANoteWithHider(),
        HiderWidget("Folders", noteFolderUpdaterComp).hider(),
        <.br,
        <.br
      )
    }

    val rootComp =
      ScalaComponent
        .builder[Unit]("Hello")
        .render_P(render)
        .build

    rootComp

  }

}
