package app.client.ui.routing.routeRepresentations

import app.client.ui.components.rootComponents.ItemsPage
import app.client.ui.routing.routersChildren.itemsComp.{Item1DataComp, Item2DataComp, ItemsInfoComp}
import japgolly.scalajs.react.extra.router.{RouterConfigDsl, StaticDsl}
import japgolly.scalajs.react.vdom.VdomElement

sealed abstract class Item(val title: String,
                           val routerPath: String,
                           val render: () => VdomElement)

object Item {

  case object Info extends Item("Info", "info", () => ItemsInfoComp())

  case object Item1 extends Item("Item1", "item1", () => Item1DataComp())

  case object Item2 extends Item("Item2", "item2", () => Item2DataComp())

  val menu = Vector(Info, Item1, Item2)

  val routes: StaticDsl.Rule[Item] = RouterConfigDsl[Item].buildRule { dsl =>
    import dsl._
    menu
      .map { i: Item =>
        staticRoute(i.routerPath, i) ~> renderR(
          r => ItemsPage(ItemsPage.Props(i, r)))
      }
      .reduce(_ | _)
  }
}
