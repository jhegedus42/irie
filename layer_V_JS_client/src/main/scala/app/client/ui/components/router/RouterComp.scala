package app.client.ui.components.router

import app.client.ui.caching.{CacheInjectorHOC, CacheInterface}
import app.client.ui.components.generalComponents.TopNavComp.Menu
import app.client.ui.components.generalComponents.{FooterComp, TopNavComp}
import app.client.ui.components.router.mainPageComp.{
  StaticReactCompPageExampleMPC,
  MainPageDeclaration,
  MainPage_CacheTestDemoPage,
  MainPage_HomePage
}
import app.client.ui.components.router.mainPageComp.cacheTestMPC.{
  CacheTestComp,
  CacheTest_RootComp_Props
}
import japgolly.scalajs.react.extra.router.{
  Resolution,
  RouterConfigDsl,
  RouterCtl,
  _
}
import japgolly.scalajs.react.vdom.html_<^._

// this wrapper is needed so that we can "re render the react tree below this"
// it gets the re render triggerer so that it can update-it

case class RouterComp() {

  lazy val cache = new CacheInterface()

  val cacheTestRootComp =
    CacheTestComp.compConstructor

  val wrapped_cachTestRootComp = new CacheInjectorHOC( cacheTestRootComp )

  val config = RouterConfigDsl[MainPageDeclaration].buildConfig { dsl =>
    import dsl._

    val wr =
      wrapped_cachTestRootComp.wrapperConstructor(
        CacheTest_RootComp_Props.apply(
          "These are the props via the wrapper",
          cacheInterface = cache
        )
      )

    val homeRoute: dsl.Rule = staticRoute( root, MainPage_HomePage ) ~> render(
      StaticReactCompPageExampleMPC.apply()
    )

    val cacheTestPageRoute: dsl.Rule =
      staticRoute( "#cacheTest", MainPage_CacheTestDemoPage ) ~>
        render( {
          wr
        } )

    (trimSlashes
      | homeRoute
      | cacheTestPageRoute)
      .notFound(
        redirectToPage( MainPage_HomePage )( Redirect.Replace )
      )
      .renderWith( f = layout )
  }

  val mainMenu = Vector.apply(
    Menu.apply( "Home", MainPage_HomePage ),
    Menu.apply( "CacheTest", MainPage_CacheTestDemoPage )
  )

  val baseUrl = BaseUrl.fromWindowOrigin_/

  val router =
    Router.apply( baseUrl, config )

  def layout(
      c: RouterCtl[MainPageDeclaration],
      r: Resolution[MainPageDeclaration]
  ) = {
    import bootstrap4.TB.C

    println( s"page = ${r.page}" )
    <.div.apply(
      TopNavComp.apply( TopNavComp.Props.apply( mainMenu, r.page, c ) ),
        r.render.apply()
    )
  }

}
