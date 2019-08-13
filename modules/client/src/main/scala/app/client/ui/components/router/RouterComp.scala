package app.client.ui.components.router

import app.client.ui.caching.cacheInjector.{CacheInterface, CompWrapper}
import app.client.ui.components.generalComponents.TopNavComp.Menu
import app.client.ui.components.generalComponents.{FooterComp, TopNavComp}
import app.client.ui.components.router.mainPageComponents.sumNumbers.data.{
  SumNumberState,
  SumNumbersProps
}
import app.client.ui.components.router.mainPageComponents.sumNumbers.{
  SumNumbersBackend,
  SumNumbersComponent
}
import app.client.ui.components.router.mainPageComponents.{
  CacheTestDemoPage,
  HomePage,
  MainPage,
  StaticPageExample
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

  val config = RouterConfigDsl[MainPage].buildConfig { dsl =>
    import dsl._

    val homeRoute: dsl.Rule = staticRoute( root, HomePage ) ~> render(
      StaticPageExample.apply()
    )

    val cacheTestPageRoute: dsl.Rule = {

      val compToBeWrapped = SumNumbersComponent.component

      val ppr= () => SumNumbersProps("hello world")

      val wrappedComp =
        CompWrapper[SumNumbersProps,
                    SumNumberState,
                    SumNumbersBackend[SumNumbersProps]](
          cache = cache, propsProvider = ppr, compToBeWrapped )

      staticRoute( "#cacheTest", CacheTestDemoPage ) ~>
        render( { wrappedComp.wr } )

    }

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

    println( s"page = ${r.page}" )
    <.div.apply(
      TopNavComp.apply( TopNavComp.Props.apply( mainMenu, r.page, c ) ),
      r.render.apply(),
      FooterComp()
    )
  }
}
