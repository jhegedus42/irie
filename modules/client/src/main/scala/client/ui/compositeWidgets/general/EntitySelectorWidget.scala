package client.ui.compositeWidgets.general

import client.cache.{Cache, CacheMap}
import client.sodium.core.{Cell, CellSink, StreamSink}
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

//  lazy val updaterExp=CellSink

  lazy val selectedEntity =
    new StreamSink[Option[Cell[TypedReferencedValue[V]]]]()

  lazy val selectorTable = {

    def getDomList(u: TypedReferencedValue[V]): List[VdomElement] = {

      val name: VdomElement =
        <.div(
          nameProvider(u.versionedEntityValue.valueWithoutVersion)
        )

      val selector = SButton("select", {
        val ent: Cell[TypedReferencedValue[V]] =
          c.cellLoop.map(_.map(u.ref))
        Some(() => selectedEntity.send(Some(ent)))
      })

      List(name, selector.comp())

    }

    val comp = CellTemplate(
      c.cellLoop, { x: CacheMap[V] =>
        {
          lazy val vdomList = x.map.values.toList.map(getDomList)
          <.div(TableHelpers.getTableFromVdomElements(vdomList))
        }
      }
    )

    comp
  }

}
