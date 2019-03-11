package app.client.ui.routing

import app.client.ui.components.notRootComponents.{Footer, TopNav}
import app.client.ui.components.rootComponents.HomePage
import app.client.ui.components.notRootComponents.TopNav.Menu
import app.client.ui.components.rootComponents.cacheTestRootComp.{CacheTestRootComp, CacheTestRootCompProps}
import app.client.ui.routing.routes.Item
import app.client.ui.routing.routes.Pages.{AbstrReprOfPage, CacheTest_AbstrReprOfPage, HomePage_AbstrReprOfPage, Item_AbstrReprOfPage, Items_AbstrReprOfPage}
import japgolly.scalajs.react.extra.router.StaticDsl.Route
import japgolly.scalajs.react.extra.router.{Resolution, RouterConfigDsl, RouterCtl, _}
import japgolly.scalajs.react.vdom.html_<^._
import scalacss.Defaults._
import scalacss.ScalaCssReact._
import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Js
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.extra.OnUnmount
import japgolly.scalajs.react.vdom.html_<^._

case class RouterComp(hereWillPassedTheCache: String ) {

  val itemPage = ScalaComponent
    .builder[Item_AbstrReprOfPage]( "Item page" )
    .render( {
      println( "render lefut" )
      p =>
        <.div( s"Info for item #${p.props.id}" )
    } )
    .build
  // ^^^ ennek kell adni egy state-et

  val config = RouterConfigDsl[AbstrReprOfPage].buildConfig {
    dsl =>
      import dsl._

      val dynItemRoute
        : dsl.Rule = dynamicRouteCT( "#item" / int.caseClass[Item_AbstrReprOfPage] ) ~> dynRender(
        itemPage( _ )
      )

      val itemRoutes: dsl.Rule =
        Item.routes.prefixPath_/( "#items" ).pmap[AbstrReprOfPage]( Items_AbstrReprOfPage ) {
          case Items_AbstrReprOfPage( p: Item ) => p
        }

      val homeRoute: dsl.Rule = staticRoute( root, HomePage_AbstrReprOfPage ) ~> render( HomePage() )

      val cacheTestPageRoute: dsl.Rule = staticRoute( "#cacheTest", CacheTest_AbstrReprOfPage ) ~> render( {
        val rootComp =
          CacheTestRootComp.compConstructor(CacheTestRootCompProps("These are the props"))
        rootComp
      } )

      (trimSlashes
        | homeRoute
        | cacheTestPageRoute
        | dynItemRoute
        | itemRoutes)
        .notFound( redirectToPage( HomePage_AbstrReprOfPage )( Redirect.Replace ) )
        .renderWith( layout )
  }

  val mainMenu = Vector(
    Menu( "Home", HomePage_AbstrReprOfPage ),
    Menu( "CacheTest", CacheTest_AbstrReprOfPage ),
    Menu( "Items", Items_AbstrReprOfPage( Item.Info ) )
  )

  def layout(c: RouterCtl[AbstrReprOfPage], r: Resolution[AbstrReprOfPage] ) = {

    println( s"page = ${r.page}" )
    <.div(
      TopNav( TopNav.Props( mainMenu, r.page, c ) ),
      r.render(),
      Footer() //
    )
  }

  val baseUrl = BaseUrl.fromWindowOrigin_/

  val router =
    Router( baseUrl, config )
}
