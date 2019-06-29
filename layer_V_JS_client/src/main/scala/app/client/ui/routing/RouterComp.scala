package app.client.ui.routing

import app.client.ui.routing.cache.exposed.CacheInterface
import app.client.ui.routing.generalComponents.TopNavComp.Menu
import app.client.ui.routing.generalComponents.{FooterComp, TopNavComp}
import app.client.ui.routing.canBeRoutedTo.components.{CacheTestRootComp, CacheTestRootCompProps, HomePage}
import app.client.ui.routing.canBeRoutedTo.components.DataRepresentations._
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.extra.router.{Resolution, RouterConfigDsl, RouterCtl, _}
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{CtorType, _}

case class RouterComp() {

  lazy val reRenderTriggerer = ??? // wishful thinking // TODO
  lazy val cache = new CacheInterface(reRenderTriggerer)


  val config = RouterConfigDsl[AbstrReprOfPage].buildConfig {
    dsl =>
      import dsl._


      val homeRoute: dsl.Rule = staticRoute(root, HomePage_AbstrReprOfPage) ~> render(HomePage())

      val cacheTestPageRoute: dsl.Rule =
        staticRoute("#cacheTest", CacheTest_AbstrReprOfPage) ~> render({
          val rootComp =
            CacheTestRootComp.compConstructor(
              CacheTestRootCompProps("These are the props",cacheInterface = cache))
          rootComp
        })


      (trimSlashes
        | homeRoute
        | cacheTestPageRoute)
        .notFound(redirectToPage(HomePage_AbstrReprOfPage)(Redirect.Replace))
        .renderWith(layout)
  }

  val mainMenu = Vector(
    Menu("Home", HomePage_AbstrReprOfPage),
    Menu("CacheTest", CacheTest_AbstrReprOfPage)
  )
  val baseUrl = BaseUrl.fromWindowOrigin_/
  val router =
    Router(baseUrl, config)


  def layout(c: RouterCtl[AbstrReprOfPage], r: Resolution[AbstrReprOfPage]) = {
    // CACHE
    println(s"page = ${r.page}")
    <.div(
      TopNavComp(TopNavComp.Props(mainMenu, r.page, c)),
      r.render(),
      FooterComp() //
    )
  }
}
