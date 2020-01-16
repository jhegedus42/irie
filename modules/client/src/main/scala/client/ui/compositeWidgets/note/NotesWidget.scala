package client.ui.compositeWidgets.note

import client.cache.{Cache, CacheMap}
import client.sodium.core.{CellLoop, CellSink}
import client.ui.atomicWidgets.input.SButton
import client.ui.atomicWidgets.show.text.SWPreformattedText
import client.ui.atomicWidgets.templates.CellTemplate
import client.ui.compositeWidgets.general.{
  EntityCreatorWidget,
  CellOptionDisplayerWidget,
  EntitySelectorWidget,
  TextFieldUpdaterWidget
}
import client.ui.helpers.table.TableHelpers
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}
import org.scalajs.dom.html.Div
import shared.dataStorage.{Note, TypedReferencedValue, User}
import scala.concurrent.ExecutionContextExecutor

case class NotesWidget() {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  implicit lazy val noteCache: Cache[Note] = Cache.noteCache

  val selector = EntitySelectorWidget[Note]({ x: Note =>
    x.title
  })

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

//  val noteFolderUpdater = NoteFolderUpdaterWidget(
//    selector.selectedEntity
//  )

  lazy val noteCreator = EntityCreatorWidget({ () =>
    Note("default title", "default content",None )
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
//        noteFolderUpdater.getComp(),
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
