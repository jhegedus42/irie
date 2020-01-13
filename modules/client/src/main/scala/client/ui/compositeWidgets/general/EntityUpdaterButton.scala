package client.ui.compositeWidgets.general

import client.cache.{Cache, UpdateEntityInCacheCmd}
import client.sodium.core
import client.sodium.core.Cell
import client.ui.atomicWidgets.input.SButton
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement}
import shared.dataStorage.{TypedReferencedValue, Value}

case class EntityUpdaterButton[V <: Value[V], T](
  selectedEntity: Cell[Option[TypedReferencedValue[V]]],
  cache:          Cache[V],
  extractor:      V => T,
  updaterOpt:     Cell[Option[(V, T) => V]],
  buttonLabel:    String) {

  lazy val field: core.Cell[Option[T]] =
    selectedEntity
      .map(_.map(_.versionedEntityValue.valueWithoutVersion)).map(
        _.map(extractor)
      )

  def updateCMD(
    trvOpt: Option[TypedReferencedValue[V]]
  ): Option[UpdateEntityInCacheCmd[V]] = {
    for {
      trv <- trvOpt
      newField <- field.sample()
      v = trv.versionedEntityValue.valueWithoutVersion
      updater <- updaterOpt.sample()
      newVal    = updater(v, newField)
      updateCMD = UpdateEntityInCacheCmd[V](trv, newVal)
    } yield (updateCMD)
  }

  lazy val updateButton = SButton(
    buttonLabel,
    Some({ () =>
      {
        lazy val trvOpt = selectedEntity.sample()
        lazy val cmdOpt = updateCMD(trvOpt)
        if (cmdOpt.isDefined) {
          val cmd = cmdOpt.get
          cache.updateEntityCommandStream.send(cmd)
        }
      }
    })
  )

  def comp = {

    def render: Unit => VdomElement = { _ =>
      <.div(
        <.br,
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
