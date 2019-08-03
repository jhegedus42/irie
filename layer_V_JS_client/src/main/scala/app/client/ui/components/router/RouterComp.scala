package app.client.ui.components.router

import app.client.ui.caching.{CacheInjectorHOC, CacheInterface}
import app.client.ui.components.generalComponents.TopNavComp.Menu
import app.client.ui.components.generalComponents.{FooterComp, TopNavComp}
import app.client.ui.components.router.mainPageComponents.{
  StaticPageExample,
  MainPage,
  CacheTestDemoPage,
  HomePage
}
import app.client.ui.components.router.mainPageComponents.sumNumbers.{
  CacheTestComp,
  SumNumbersExample_Props
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

  val config = RouterConfigDsl[MainPage].buildConfig { dsl =>
    import dsl._

    val wr =
      wrapped_cachTestRootComp.wrapperConstructor(
        SumNumbersExample_Props.apply(
          "These are the props via the wrapper",
          cacheInterface = cache
        )
      )

    val homeRoute: dsl.Rule = staticRoute( root, HomePage ) ~> render(
      StaticPageExample.apply()
    )

    val cacheTestPageRoute: dsl.Rule =
      staticRoute( "#cacheTest", CacheTestDemoPage ) ~>
        render( {
          wr
        } )

    (trimSlashes
      | homeRoute
      | cacheTestPageRoute)
      .notFound(
        redirectToPage( HomePage )( Redirect.Replace )
      )
      .renderWith( f = layout )
  }

  val mainMenu = Vector.apply(
    Menu.apply( "Home", HomePage ),
    Menu.apply( "CacheTest", CacheTestDemoPage )
  )

  val baseUrl = BaseUrl.fromWindowOrigin_/

  val router =
    Router.apply( baseUrl, config )

  def layout(
              c: RouterCtl[MainPage],
              r: Resolution[MainPage]
  ) = {
    import bootstrap4.TB.C

    println( s"page = ${r.page}" )
    <.div.apply(
      TopNavComp.apply( TopNavComp.Props.apply( mainMenu, r.page, c ) ),
        r.render.apply(),
      FooterComp()
    )
  }

}
