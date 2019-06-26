package app.client.ui.routing.generalComponents

import app.client.ui.routing.RouterComp
import app.client.ui.routing.dataRepresentersOfToRoutableComponents.Pages.AbstrReprOfPage
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.Reusability
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import scalacss.Defaults._
import scalacss.ScalaCssReact._


object TopNavComp {


  object Style extends StyleSheet.Inline {

    import dsl._

    val navMenu = style(display.flex,
                        alignItems.center,
                        backgroundColor.gray,
                        margin.`0`,
                        listStyle := "none")

    val menuItem = styleF.bool { selected =>
      styleS(
        padding(20.px),
        fontSize(1.5.em),
        cursor.pointer,
        color(c"rgb(244, 233, 233)"),
        mixinIfElse(selected)(color(c"#504747"), fontWeight._500)(
          &.hover(backgroundColor.darkgray))
      )
    }
  }

  // this is used in the props
  case class Menu(name: String, route: AbstrReprOfPage)

  case class Props(menus: Vector[Menu],
                   selectedPage: AbstrReprOfPage,
                   ctrl: RouterCtl[AbstrReprOfPage])

  implicit val currentPageReuse = Reusability.by_==[AbstrReprOfPage]
  implicit val propsReuse = Reusability.by((_: Props).selectedPage)

  val component = ScalaComponent
    .builder[Props]("TopNav")
    .render_P { P =>
      <.header(
        <.nav(
          <.ul(
            Style.navMenu,
            P.menus.toTagMod { item =>
              <.li(
                ^.key := item.name,
                Style.menuItem(item.route.getClass == P.selectedPage.getClass),
                item.name,
                P.ctrl setOnClick item.route
              )
            }
          )
        )
      )
    }
    .configure(Reusability.shouldComponentUpdate)
    .build

  def apply(props: Props) = component(props)

}
