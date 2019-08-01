package app.client.ui.components.generalComponents
import app.client.ui.components.router.mainPageComp.MainPageDeclaration
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.Reusability
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import scalacss.Defaults._
import scalacss.ScalaCssReact._
import bootstrap4.TB.C

object TopNavComp {


  // this is used in the props
  case class Menu(name: String, route: MainPageDeclaration )

  case class Props(
      menus:        Vector[Menu],
      selectedPage: MainPageDeclaration,
      ctrl:         RouterCtl[MainPageDeclaration])

  implicit val currentPageReuse = Reusability.by_==[MainPageDeclaration]
  implicit val propsReuse = Reusability.by( (_: Props).selectedPage )

  val component = ScalaComponent
    .builder[Props]( "TopNav" )
    .render_P { P: Props =>
      <.header(
        <.nav( C.navbar, C.navbarDark, C.mb4, C.navbarExpandMd, C.bgDark,
          <.ul( C.navbarNav, C.mrAuto,
            P.menus.toTagMod { item: Menu =>
              <.li(
                C.navItem,
                C.navLink,
                ^.key := item.name,
                {if(P.selectedPage == item.route) C.active else C.navLink},
                // todo-now :
                //  Put the bootstrap navbar button into this file, subtasks:
                //    1) put the bootstrap javascript / jquery librariers into
                //       the index.html file. >>>>>>> TICK !!!
                //    ---------------------------------------------
                //    2) put these libs into the server generated index.thml file
                //    ---------------------------------------------
                //    3) Use ochron's SPA example (clone it, compile it,
                //       run it).
                //    ---------------------------------------------
                //    4) lift the needed from the SPA example code into this file
                //
                item.name,
                P.ctrl setOnClick item.route
              )
            }
          )
        )
      )
    }
    .configure( Reusability.shouldComponentUpdate )
    .build

  def apply(props: Props ) = component( props )

}
