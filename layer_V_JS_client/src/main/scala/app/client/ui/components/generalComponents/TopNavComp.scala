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
        <.nav(
          C.navbar,
          C.navbarLight,
          C.navbarExpand,
          C.bgLight,
          <.ul(
            C.navbarNav,
            P.menus.toTagMod { item: Menu =>
              <.li(
                C.navItem,
                C.navLink,
                ^.key := item.name,
//                Style.menuItem( item.route.getClass == P.selectedPage.getClass ),
                // TODO-one-day ^^^ make this work with bootstrap

                // todo-now :
                //  Put the bootstrap navbar button into this file, subtasks:
                //    1) put the bootstrap javascript / jquery librariers into
                //       the index.html file.
                //    2) Use ochron's SPA example for this (clone it, compile it,
                //       run it, learn from it).
                //    3) Look at www/examples/bs/index.html:61
                //    4) Look into what is `<main` there.
                //    5) Look into relax-ng
                //    6) Look into relax-ng HTML 5 schema :
                //        - when I click CMD+B on `<main` then what is that file I get ?
                //          - look for some IntelliJ plugin for that.
                //          - look into installing/buying IntelliJ Ultimate for
                //            webdev/JS support, if needed
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
