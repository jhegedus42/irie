package app.client.ui.components.router

import app.client.ui.caching.cacheInjector.{CacheInterface, CompWrapper, CompWrapper2}
import app.client.ui.components.generalComponents.TopNavComp.Menu
import app.client.ui.components.generalComponents.{FooterComp, TopNavComp}
import app.client.ui.components.router.mainPageComponents.sumNumbers.data.{SumNumberState, SumNumbersProps}
import app.client.ui.components.router.mainPageComponents.sumNumbers.{SumNumbersBackend, SumNumbersComponent, SumNumbersType}
import app.client.ui.components.router.mainPageComponents.{SumIntDemo, HomePage, MainPage, StaticPageExample}
import japgolly.scalajs.react.extra.router.{Resolution, RouterConfigDsl, RouterCtl, _}
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

    val sumNumberCompRoute: dsl.Rule = {

      val wc2=CompWrapper2[SumNumbersType](
        cache = cache,
        propsProvider = () => SumNumbersProps("hello world"),
        comp=SumNumbersComponent.component
      )

      staticRoute( "#cacheTest", SumIntDemo ) ~>
          render( { wc2.wr } ) //todo-now <= make this dynamic

    }

    (trimSlashes
      | homeRoute
      | sumNumberCompRoute)
      .notFound(
        redirectToPage( HomePage )( Redirect.Replace )
      )
      .renderWith( f = layout )
  }

  val mainMenu = Vector.apply(
    Menu.apply( "Home", HomePage ),
    Menu.apply( "SumIntDemo", SumIntDemo )
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
