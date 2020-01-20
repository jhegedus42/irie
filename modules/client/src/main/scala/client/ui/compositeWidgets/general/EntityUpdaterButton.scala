package client.ui.compositeWidgets.general

import client.cache.Cache
import client.cache.commands.UpdateEntityInCacheCmd
import client.cache.relationalOperations.CellOptionMonad.CellOption
import client.sodium.core
import client.sodium.core.Cell
import client.ui.atomicWidgets.input.SButton
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement}
import shared.dataStorage.model.Value
import shared.dataStorage.relationalWrappers.TypedReferencedValue

case class EntityUpdaterButton[V <: Value[V]](
  selectedEntityOpt: CellOption[TypedReferencedValue[V]],
  cache:             Cache[V],
  updater:           CellOption[V => V],
  buttonLabel:       String) {

  lazy val updaterButton = SButton(
    buttonLabel,
    Some({ () =>
      {

        lazy val trvOpt = selectedEntityOpt.co.sample()

        val currentVal: CellOption[V] = selectedEntityOpt.map(
          _.versionedEntityValue.valueWithoutVersion
        )

        lazy val newValueOpt: Option[V] =
          currentVal.applicativeMap(updater).co.sample()

        if (trvOpt.isDefined && newValueOpt.isDefined) {
          val updateCMD =
            UpdateEntityInCacheCmd[V](trvOpt.get, newValueOpt.get)
          cache.updateEntityCommandStream.send(updateCMD)
        }
      }
    })
  )

  def comp = {

    def render: Unit => VdomElement = { _ =>
      <.div(
        <.br,
        updaterButton.comp(),
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
