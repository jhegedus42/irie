package client.ui.compositeWidgets.general

import client.cache.{Cache, CacheMap}
import client.sodium.core.{Cell, CellSink, StreamSink}
import client.ui.atomicWidgets.input.SButton
import client.ui.atomicWidgets.templates.CellTemplate
import client.ui.helpers.table.TableHelpers
import japgolly.scalajs.react.vdom.TagOf
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement}
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}
import org.scalajs.dom.html.Div
import shared.dataStorage.model.Value
import shared.dataStorage.relationalWrappers.{Ref, TypedReferencedValue, UnTypedRef}

case class EntitySelectorWidget[V <: Value[V]](
  nameProvider: V => TagOf[Div]
)(
  implicit
  c: Cache[V]) {

  lazy val initCell = new Cell[Option[TypedReferencedValue[V]]](None)

  lazy val selectedEntityInjector =
    new StreamSink[Cell[Option[TypedReferencedValue[V]]]]()

  lazy val selectedEntityRefInjector =
    new StreamSink[Option[Ref[V]]]()

  lazy val refToSelectedEntity: Cell[Option[Ref[V]]] =
    selectedEntityRefInjector.hold(None)

  lazy val selectedEntityResolved : Cell[Option[TypedReferencedValue[V]]] = {
    Cache.resolveRef(refToSelectedEntity)
  }


//  lazy val selectedEntity: Cell[Option[TypedReferencedValue[V]]] =
//    Cell.switchC(selectedEntityInjector.hold(initCell))

  lazy val selectorTable = {

    def getDomList(u: TypedReferencedValue[V]): List[VdomElement] = {

      val name: VdomElement =
        <.div(
          nameProvider(u.versionedEntityValue.valueWithoutVersion)
        )

      val selector = SButton("select", {

//        val ent: Cell[Option[TypedReferencedValue[V]]] =
//          c.cellLoop.map(_.cacheMap.get(u.ref))

//        Some(() => selectedEntityInjector.send(ent))
        //        ???

        Some(() => selectedEntityRefInjector.send(Some(u.ref)))

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
