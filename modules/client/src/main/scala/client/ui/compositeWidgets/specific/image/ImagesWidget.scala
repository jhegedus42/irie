package client.ui.compositeWidgets.specific.image

import client.cache.{Cache, CacheMap}
import client.sodium.core.{CellLoop, CellSink}
import client.ui.atomicWidgets.input.SButton
import client.ui.atomicWidgets.show.text.SWPreformattedText
import client.ui.atomicWidgets.templates.CellTemplate
import client.ui.compositeWidgets.general.{
  CellOptionDisplayerWidget,
  EntitySelectorWidget,
  TextFieldUpdaterWidget
}
import client.ui.helpers.table.TableHelpers
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}
import org.scalajs.dom.html.Div
import shared.dataStorage.{ImageWithQue, Note, TypedReferencedValue, User}

import scala.concurrent.ExecutionContextExecutor
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement}

case class ImagesWidget() {

  implicit lazy val imgCache: Cache[ImageWithQue] = Cache.imgCache

  val selector = EntitySelectorWidget[ImageWithQue]({ x: ImageWithQue =>
    x.title
  })

  def getComp = {


    def render: Unit => VdomElement = { _ =>
      <.div(
        <.hr,
        <.h2("Images"),
        <.br,
        selector.selectorTable.comp(),
        <.br,
        ImageUploaderWidget(selector.selectedEntity, imgCache)
          .render(),
        ImageDisplayerWidget(selector.selectedEntity).imageDisplayer()
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
