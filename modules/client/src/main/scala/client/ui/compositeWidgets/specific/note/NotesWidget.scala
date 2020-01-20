package client.ui.compositeWidgets.specific.note

import client.cache.relationalOperations.CellOptionMonad.CellOption
import client.cache.relationalOperations.NoteOperations
import client.cache.relationalOperations.RelationalOperations.ResultSet
import client.cache.{Cache, CacheMap}
import client.sodium.core.{CellLoop, CellSink}
import client.ui.atomicWidgets.input.SButton
import client.ui.atomicWidgets.show.text.SWPreformattedText
import client.ui.atomicWidgets.templates.CellTemplate
import client.ui.compositeWidgets.general.{
  CellOptionDisplayerWidget,
  CellOptionListWidget,
  EntityCreatorWidget,
  EntitySelectorWidget,
  TextFieldUpdaterWidget
}
import client.ui.helpers.table.TableHelpers
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}
import org.scalajs.dom.html.Div
import shared.dataStorage.{
  ImageWithQue,
  Note,
  TypedReferencedValue,
  User
}

import scala.concurrent.ExecutionContextExecutor

case class NotesWidget() {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  implicit lazy val noteCache: Cache[Note] = Cache.noteCache

  lazy val selector = EntitySelectorWidget[Note]({ x: Note =>
    x.title
  })



  import client.cache.relationalOperations.RelationalOperations.Pipe

  lazy val selectedNote =
    selector.selectedEntity |> CellOption.fromCellOption

  lazy val noteTitleEditor = TextFieldUpdaterWidget[Note](
    "title",
    selector.selectedEntity,
    noteCache, { n: Note =>
      n.title
    }, { (n: Note, s: String) =>
      n.copy(title = s)
    }
  )

  // todo-now - note folder editor

  lazy val imagesComp = {

    import client.cache.relationalOperations.RelationalOperations._

    val is: CellOption[List[ImageWithQue]] =
      NoteOperations
        .getImagesForANote(selector.selectedEntity)
        .map(_ |> toVal)
        .map(_.toList)

    val f = (i: ImageWithQue) => <.div(i.title)

    CellOptionListWidget[ImageWithQue](
      is,
      CellOption.apply(f)
    )
  }

  val noteFolderUpdater = NoteFolderUpdaterWidget(
    selector.selectedEntity
  )

  lazy val noteCreator = EntityCreatorWidget({ () =>
    Note("default title", "default content")
  }, "Note")

  def getComp = {

    def render: Unit => VdomElement = { _ =>
      <.div(
        <.hr,
        <.h2("Notes"),
        <.br,
        selector.selectorTable.comp(),
        noteCreator.createNewEntityButton.comp(),
        noteTitleEditor.comp(),
        noteFolderUpdater.getComp(),
        <.br,
        s"Images that refer to this Note:",
        <.br,
        imagesComp.comp(),
        <.hr,
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
