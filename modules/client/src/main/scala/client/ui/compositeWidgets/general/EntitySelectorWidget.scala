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

  lazy val initCell = new Cell[Option[TypedReferencedValue[V]]](None)

  lazy val selectedEntityInjector =
    new StreamSink[Cell[Option[TypedReferencedValue[V]]]]()

  lazy val selectedEntity: Cell[Option[TypedReferencedValue[V]]] =
    Cell.switchC(selectedEntityInjector.hold(initCell))

  lazy val selectorTable = {

    def getDomList(u: TypedReferencedValue[V]): List[VdomElement] = {

      val name: VdomElement =
        <.div(
          nameProvider(u.versionedEntityValue.valueWithoutVersion)
        )

      val selector = SButton("select", {
        val ent: Cell[Option[TypedReferencedValue[V]]] =
          c.cellLoop.map(_.cacheMap.get(u.ref))
        Some(() => selectedEntityInjector.send(ent))
      })

      List(name, selector.comp())

    }

    val comp = CellTemplate(
      c.cellLoop, { x: CacheMap[V] =>
        {
          lazy val vdomList = x.cacheMap.values.toList.map(getDomList)
          <.div(TableHelpers.getTableFromVdomElements(vdomList))
        }
      }
    )

    comp
  }

}
