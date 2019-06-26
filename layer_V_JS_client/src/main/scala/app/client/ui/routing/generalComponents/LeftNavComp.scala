package app.client.ui.routing.generalComponents

import app.client.ui.routing.dataRepresentersOfToRoutableComponents.Item
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.Reusability
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import scalacss.Defaults._
import scalacss.ScalaCssReact._

object LeftNavComp {

  object Style extends StyleSheet.Inline {

    import dsl._

    val container = style(display.flex,
                          flexDirection.column,
                          listStyle := "none",
                          padding.`0`)

    val menuItem = styleF.bool { selected =>
      styleS(
        lineHeight(48.px),
        padding :=! "0 25px",
        cursor.pointer,
        textDecoration := "none",
        mixinIfElse(selected)(color.white, fontWeight._500,backgroundColor.darkslategrey)(
          color.black,
          &.hover(color.black, backgroundColor.darkgrey)
        )
      )
    }
  }

  case class Props(menus: Vector[Item],
                   selectedPage: Item,
                   ctrl: RouterCtl[Item])

  implicit val currentPageReuse = Reusability.by_==[Item]
  implicit val propsReuse = Reusability.by((_: Props).selectedPage)

  val component = ScalaComponent
    .builder[Props]("LeftNav")
    .render_P { P =>
      <.ul(
        Style.container,
        P.menus.toTagMod(
          item =>
            <.li(^.key := item.title,
                 Style.menuItem(item == P.selectedPage),
                 item.title,
                 P.ctrl setOnClick item)
        )
      )
    }
    .configure(Reusability.shouldComponentUpdate)
    .build

  def apply(props: Props) = component(props)

}
