package client.ui.compositeWidgets.image

import client.cache.{Cache, CacheMap}
import client.sodium.core.{CellLoop, CellSink}
import client.ui.atomicWidgets.input.SButton
import client.ui.atomicWidgets.show.text.SWPreformattedText
import client.ui.atomicWidgets.templates.CellTemplate
import client.ui.compositeWidgets.general.{
  EntityCreatorWidget,
  EntitySelectorWidget,
  TextFieldUpdaterWidget
}
import client.ui.helpers.table.TableHelpers
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}
import org.scalajs.dom.html.Div
import shared.dataStorage.{Image, Note, TypedReferencedValue, User}

import scala.concurrent.ExecutionContextExecutor
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement}

case class ImagesWidget() {

  implicit lazy val imgCache: Cache[Image] = Cache.imgCache

  val selector = EntitySelectorWidget[Image]({ x: Image=>
    x.title
  })

  def getComp = {

    def render: Unit => VdomElement = { _ =>
      <.div(
        <.hr,
        <.h2("Images"),
        <.br,
        selector.selectorTable.comp(),
        <.hr,
        <.br,
        ImageUploaderWidget().render()
      )
    }

    val rootComp =
      ScalaComponent
        .builder[Unit]("ImagesWidget")
        .render_P(render)
        .build

    rootComp

  }

}
