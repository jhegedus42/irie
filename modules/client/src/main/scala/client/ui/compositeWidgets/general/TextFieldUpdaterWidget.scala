package client.ui.compositeWidgets.general

import client.cache.Cache
import client.cache.commands.UpdateEntityInCacheCmd
import client.sodium.core
import client.sodium.core.Cell
import client.ui.atomicWidgets.input.{SButton, STextArea}
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement}
import client.cache.{Cache, CacheMap}
import client.sodium.core.{Cell, CellLoop, CellSink}
import client.ui.atomicWidgets.input.{SButton, STextArea}
import client.ui.atomicWidgets.show.text.SWPreformattedText
import client.ui.atomicWidgets.templates.CellTemplate
import client.ui.helpers.table.TableHelpers
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}
import org.scalajs.dom.html.Div
import monocle.macros.syntax.lens._

import scala.concurrent.ExecutionContextExecutor
import client.sodium.core.{CellLoop, Stream, StreamSink, Transaction}
import shared.dataStorage.model.Value
import shared.dataStorage.relationalWrappers.TypedReferencedValue

case class TextFieldUpdaterWidget[V <: Value[V]](
  fieldName: String,
  cell:      Cell[Option[TypedReferencedValue[V]]],
  cache: Cache[V],
  extractor: V => String,
  updater:   (V, String) => V) {

  lazy val field: core.Stream[String] =
    cell
      .map(_.map(_.versionedEntityValue.valueWithoutVersion)).map(
        _.map(extractor).getOrElse("")
      ).updates()

  lazy val fieldEditor: STextArea = STextArea("", field)

  def updateCMD(
    trvOpt: Option[TypedReferencedValue[V]]
  ): Option[UpdateEntityInCacheCmd[V]] = {
    for {
      trv <- trvOpt
      v         = trv.versionedEntityValue.valueWithoutVersion
      newField  = fieldEditor.cell.sample()
      newVal    = updater(v, newField)
      updateCMD = UpdateEntityInCacheCmd[V](trv, newVal)
    } yield (updateCMD)
  }



  lazy val updateButton = SButton("update", Some({
    ()=>{
      lazy val trvOpt = cell.sample()
      lazy val cmdOpt = updateCMD(trvOpt)
      if(cmdOpt.isDefined) {
        val cmd=cmdOpt.get
        cache.updateEntityCommandStream.send(cmd)
      }
    }
  }))

  def comp = {

    def render: Unit => VdomElement = { _ =>
      <.div(
        <.br,
        fieldName,
        fieldEditor.comp(),
        updateButton.comp(),
        <.br
      )
    }



    val comp =
      ScalaComponent
        .builder[Unit]("Hello")
        .render_P(render)
        .build

    comp

  }

}
