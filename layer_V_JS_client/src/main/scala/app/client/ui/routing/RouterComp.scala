package app.client.ui.routing

import app.client.ui.routing.routersChildren.TopNavComp.Menu
import app.client.ui.routing.routersChildren.{FooterComp, TopNavComp}
import app.client.ui.components.rootComponents.HomePage
import app.client.ui.components.rootComponents.cacheTestRootComp.{CacheTestRootComp, CacheTestRootCompProps}
import app.client.ui.routing.routeRepresentations.Item
import app.client.ui.routing.routeRepresentations.Pages._
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.extra.router.{Resolution, RouterConfigDsl, RouterCtl, _}
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{CtorType, _}

case class RouterComp(hereWillPassedTheCache: String) {

  val itemPage: Component[Item_AbstrReprOfPage, Unit, Unit, CtorType.Props] = ScalaComponent
    .builder[Item_AbstrReprOfPage]("Item page")
    .render({
      println("render lefut")
      p =>
        <.div(s"Info for item #${p.props.id}")
    })
    .build
  // ^^^ ennek kell adni egy state-et

  val config = RouterConfigDsl[AbstrReprOfPage].buildConfig {
    dsl =>
      import dsl._

      val dynItemRoute
      : dsl.Rule = dynamicRouteCT("#item" / int.caseClass[Item_AbstrReprOfPage]) ~> dynRender(
        page => itemPage(page)
      )

      val itemRoutes: dsl.Rule =
        Item.routes.prefixPath_/("#items").pmap[AbstrReprOfPage](Items_AbstrReprOfPage) {
          case Items_AbstrReprOfPage(p: Item) => p
        }

      val homeRoute: dsl.Rule = staticRoute(root, HomePage_AbstrReprOfPage) ~> render(HomePage())

      val cacheTestPageRoute: dsl.Rule =
        staticRoute("#cacheTest", CacheTest_AbstrReprOfPage) ~> render({
          val rootComp =
            CacheTestRootComp.compConstructor(CacheTestRootCompProps("These are the props"))
          rootComp
        })

      (trimSlashes
        | homeRoute
        | cacheTestPageRoute
        | dynItemRoute
        | itemRoutes)
        .notFound(redirectToPage(HomePage_AbstrReprOfPage)(Redirect.Replace))
        .renderWith(layout)
  }

  val mainMenu = Vector(
    Menu("Home", HomePage_AbstrReprOfPage),
    Menu("CacheTest", CacheTest_AbstrReprOfPage),
    Menu("Items", Items_AbstrReprOfPage(Item.Info))
  )
  val baseUrl = BaseUrl.fromWindowOrigin_/
  val router =
    Router(baseUrl, config)

  def layout(c: RouterCtl[AbstrReprOfPage], r: Resolution[AbstrReprOfPage]) = {

    println(s"page = ${r.page}")
    <.div(
      TopNavComp(TopNavComp.Props(mainMenu, r.page, c)),
      r.render(),
      FooterComp() //
    )
  }
}
