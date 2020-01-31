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
import client.ui.compositeWidgets.general.{
  CellOptionDisplayerWidget,
  CellOptionListWidget,
  EntityCreatorWidget,
  EntitySelectorWidget,
  TextFieldUpdaterWidget
}
import client.ui.compositeWidgets.specific.image.ImagesForANote
import client.ui.compositeWidgets.specific.image.svg.CompositeSVGDisplayer
import client.ui.helpers.table.TableHelpers
import japgolly.scalajs.react.{CtorType, ScalaComponent}
import japgolly.scalajs.react.component.Scala.{Component, Unmounted}
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}
import org.scalajs.dom.html.Div
import shared.dataStorage.model.{
  CanProvideDefaultValue,
  HintForNote,
  Note
}
import shared.dataStorage.relationalWrappers.TypedReferencedValue

import scala.concurrent.ExecutionContextExecutor

case class NotesWidget() {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  implicit lazy val noteCache: Cache[Note] = Cache.noteCache


  import client.cache.relationalOperations.RelationalOperations.Pipe







  def getComp = {

    def render: Unit => VdomElement = { _ =>
      <.div(
        <.hr,
        <.h2("Notes"),
        <.br,
        <.br,
        <.br
      )
    }

    // todo-now : lapozgato - show next note, show previous note...

    val rootComp =
      ScalaComponent
        .builder[Unit]("Hello")
        .render_P(render)
        .build

    rootComp

  }

}
