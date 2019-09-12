package app.client.ui.components.router

import app.client.ui.caching.cacheInjector.{
  CacheInterface,
  ReactCompWrapper
}
import app.client.ui.components.generalComponents.TopNavComp.Menu
import app.client.ui.components.generalComponents.{
  FooterComp,
  TopNavComp
}
import app.client.ui.components.router.mainPageComponents.adminPage.StaticAdminPage
import app.client.ui.components.router.mainPageComponents.sumNumbers.{
  SumNumbersComponent,
  SumNumbersPage
}
import app.client.ui.components.router.mainPageComponents._
import app.client.ui.components.router.mainPageComponents.sumNumbers.SumNumbersPage.SumNumbersProps
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


  def getWrappedReactCompConstructor = ???

  val config = RouterConfigDsl[MainPage].buildConfig { dsl =>
    import dsl._

    val homeRoute: dsl.Rule = staticRoute(root, HomePage) ~> render(
      StaticPageExample.apply()
    )

    val sumNumberCompRoute: dsl.Rule = {

      val reactCompWrapper = ReactCompWrapper[SumNumbersPage](
        cache         = cache,
        propsProvider = () => SumNumbersProps("hello world"),
        comp          = SumNumbersComponent.component
      )

      staticRoute("#cacheTest", SumIntDemo) ~>
        render({
          reactCompWrapper.wrappedConstructor
        })

    }

    val adminPage
      : dsl.Rule = staticRoute("#admin", AdminPage) ~> render(
      StaticAdminPage.apply()
    )

    val itemPage: dsl.Rule = {

      val itemPage = japgolly.scalajs.react.ScalaComponent
        .builder[ItemPage]("Item page")
        .render(p => <.div(s"Info for item #${p.props.id}"))
        .build

      dynamicRouteCT("item" / int.caseClass[ItemPage]) ~> (dynRender(
        itemPage(_: ItemPage)
      ))

    }

    (trimSlashes
      | homeRoute
      | sumNumberCompRoute
      | itemPage
      | adminPage)
      .notFound(
        redirectToPage(HomePage)(Redirect.Replace)
      )
      .renderWith(f = layout)
  }

  val mainMenu = Vector.apply(
    Menu.apply("Home", HomePage),
    Menu.apply("SumIntDemo", SumIntDemo),
    Menu.apply("ItemPage 4", ItemPage(4)),
    Menu.apply("ItemPage 42", ItemPage(42)),
    Menu.apply("Admin Page", AdminPage)
  )
  val baseUrl = BaseUrl.fromWindowOrigin_/

  val router =
    Router.apply(baseUrl, config)

  def layout(
    c: RouterCtl[MainPage],
    r: Resolution[MainPage]
  ) = {
    <.div.apply(
      TopNavComp.apply(TopNavComp.Props.apply(mainMenu, r.page, c)),
      r.render.apply(),
      FooterComp()
    )
  }
}
