package client.ui.compositeWidgets.noteFolder

import client.cache.Cache
import client.sodium.core.Cell
import client.ui.atomicWidgets.show.text.CellPreformattedText
import client.ui.compositeWidgets.general.EntitySelectorWidget
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement}
import shared.dataStorage.{Note, NoteFolder}
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}
import japgolly.scalajs.react.ScalaComponent

import scala.concurrent.ExecutionContextExecutor

// todo-now add selector for note folder
case class NoteFolderWidget() {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  implicit lazy val noteFolderCache: Cache[NoteFolder] =
    Cache.noteFolderCache

  val selector = EntitySelectorWidget[NoteFolder]({ x: NoteFolder =>
    x.name
  })

  val selectedNoteFoldersName: Cell[String] =
    selector.selectedEntity.map(
      _.map(_.versionedEntityValue.valueWithoutVersion.name)
        .getOrElse("")
    )

  val selectedNoteFoldersNameComp = CellPreformattedText(
    selectedNoteFoldersName
  )

  def getComp = {

    def render: Unit => VdomElement = { _ =>
      <.div(
        <.hr,
        <.h2("Note Folders"),
        <.br,
        selector.selectorTable.comp(),
        selectedNoteFoldersNameComp.comp(),
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

  // todo-now 1: display note folder of a selected note
  // todo-now 2: set note folder of a selected note to
  //             one of the possible note folders

}
