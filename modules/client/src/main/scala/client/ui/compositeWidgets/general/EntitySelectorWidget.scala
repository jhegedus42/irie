package client.ui.compositeWidgets.general

import client.cache.{Cache, CacheMap}
import client.sodium.core.CellSink
import client.ui.atomicWidgets.input.SButton
import client.ui.atomicWidgets.templates.CellTemplate
import client.ui.helpers.table.TableHelpers
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement}
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}
import shared.dataStorage.{Note, TypedReferencedValue, Value}
import org.scalajs.dom.html.Div

case class EntitySelectorWidget[V <: Value[V]](
  c:            Cache[V],
  nameProvider: V => String) {

  lazy val selectedEntity =
    new CellSink[Option[TypedReferencedValue[V]]](None)

  lazy val selectorTable = {

    def getDomList(u: TypedReferencedValue[V]): List[VdomElement] = {

      val name: VdomElement =
        <.div(
          nameProvider(u.versionedEntityValue.valueWithoutVersion)
        )

      val selector = SButton("select", {
        Some(() => selectedEntity.send(Some(u)))
      })

      List(name, selector.comp())

    }

    val comp = CellTemplate(
      c.cellLoop, { x: CacheMap[V] =>
        <.div(
          TableHelpers.getTableFromVdomElements(
            x.map.values.toList
              .map(
                getDomList
              )
          )
        )
      }
    )

    comp
  }

}
