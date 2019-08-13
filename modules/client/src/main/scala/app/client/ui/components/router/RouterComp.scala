package app.client.ui.components.router

import app.client.ui.caching.{
  CacheInjectorHOC,
  CacheInterface,
  CacheInterfaceWrapper
}
import app.client.ui.components.generalComponents.TopNavComp.Menu
import app.client.ui.components.generalComponents.{FooterComp, TopNavComp}
import app.client.ui.components.router.mainPageComponents.sumNumbers.injected.localState.SumNumberState
import app.client.ui.components.router.mainPageComponents.sumNumbers.injected.{
  SumNumbersBackend,
  SumNumbersComponent,
  SumNumbersProps
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

    val homeRoute: dsl.Rule = staticRoute(root, HomePage) ~> render(
      StaticPageExample.apply()
    )

    val cacheTestPageRoute: dsl.Rule =
      staticRoute("#cacheTest", CacheTestDemoPage) ~>
        render({
          SumNumbersComp.wr
        }) // todo-now : make this route dynamic, extract a string from
    // the route and print it to the screen (inside the react page)
    // pass it from here as a Prop
    // for that this `wr` has to be a function

    (trimSlashes
      | homeRoute
      | cacheTestPageRoute)
      .notFound(
        redirectToPage(HomePage)(Redirect.Replace)
      )
      .renderWith(f = layout)
  }
  val mainMenu = Vector.apply(
    Menu.apply("Home", HomePage),
    Menu.apply("CacheTest", CacheTestDemoPage)
  )
  val baseUrl = BaseUrl.fromWindowOrigin_/
  val router =
    Router.apply(baseUrl, config)

  def layout(
              c: RouterCtl[MainPage],
              r: Resolution[MainPage]
            ) = {

    println(s"page = ${r.page}")
    <.div.apply(
      TopNavComp.apply(TopNavComp.Props.apply(mainMenu, r.page, c)),
      r.render.apply(),
      FooterComp()
    )
  }

  object SumNumbersComp { // todo-next
    // all this object here can be wrapped into a
    // a function

    val sumNumbersComp = SumNumbersComponent.component

    val wrapped_cachTestRootComp =
      new CacheInjectorHOC[SumNumbersBackend[SumNumbersProps], SumNumbersProps, SumNumberState](
        sumNumbersComp
      )

    val ciw: CacheInterfaceWrapper[SumNumbersProps] =
      CacheInterfaceWrapper[SumNumbersProps](
        cacheInterface = cache,
        props = SumNumbersProps("props 42 for the sum numbers")
      )

    val wr = wrapped_cachTestRootComp.wrapperConstructor(ciw)

  }

}
