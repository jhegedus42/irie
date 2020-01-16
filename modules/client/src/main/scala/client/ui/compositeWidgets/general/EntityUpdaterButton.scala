package client.ui.compositeWidgets.general

import client.cache.{Cache, UpdateEntitiesInCacheCmd}
import client.sodium.core
import client.sodium.core.Cell
import client.ui.atomicWidgets.input.SButton
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement}
import shared.dataStorage.{TypedReferencedValue, Value}

case class EntityUpdaterButton[V <: Value[V], T](
  selectedEntity: Cell[Option[TypedReferencedValue[V]]],
  cache:          Cache[V],
  setter:         (T,V) => V,
  newValue:       Cell[T],
  buttonLabel:    String) {

  lazy val currentValueC: core.Cell[Option[V]] =
    selectedEntity
      .map(_.map(_.versionedEntityValue.valueWithoutVersion))

  def updateCMD(
    trvOpt: Option[TypedReferencedValue[V]]
  ): Option[UpdateEntitiesInCacheCmd[V]] = {
    for {
      curVal <- currentValueC.sample()
      newField = newValue.sample()
      trv <- selectedEntity.sample()
      newVal    = setter(newField,curVal)
      updateCMD = UpdateEntitiesInCacheCmd[V](trv, newVal)
    } yield (updateCMD)
  }

  lazy val updaterButton = SButton(
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
