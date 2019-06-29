package app.client.ui.routing

import app.client.ui.routing.cache.exposed.{CacheInterface }
import app.client.ui.routing.generalComponents.TopNavComp.Menu
import app.client.ui.routing.generalComponents.{FooterComp, TopNavComp}
import app.client.ui.routing.canBeRoutedTo.components.{CacheTestComp, CacheTestRootCompProps, HomePage}
import app.client.ui.routing.canBeRoutedTo.DataRepresentations._
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.extra.router.{Resolution, RouterConfigDsl, RouterCtl, _}
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{CtorType, _}

// this wrapper is needed so that we can "re render the react tree below this"

class Wrapper(toBeWrapped:Component[CacheTestRootCompProps, Unit , _, CtorType.Props] ) {


  class WrapperBackend($: BackendScope[CacheTestRootCompProps, Int]) {
    def render( props: CacheTestRootCompProps) = {
      <.div(
        toBeWrapped(props)
      )

    }
  }

  lazy val wrapperBackend=
    ScalaComponent
      .builder[CacheTestRootCompProps,Int]("Wrapper")
      .renderBackend[WrapperBackend]
      .componentWillMount($ => {
//        $.props.cacheInterface.
        f : () => () = {
          $.modState()
        }
        val reRenderTriggerer=ReRenderTriggerer()
        this.setReRenderTriggererOnce()
      })

      ^^^^WE CONTINUE HERE WITH THIS TERRIBLE HACK

      .build

}


case class RouterComp() {




  val cacheTestRootComp =
    CacheTestComp.compConstructor(
      CacheTestRootCompProps("These are the props", cacheInterface = cache))

  lazy val cache = new CacheInterface()


  val config = RouterConfigDsl[AbstrReprOfPage].buildConfig {
    dsl =>
      import dsl._


      val homeRoute: dsl.Rule = staticRoute(root, HomePage_AbstrReprOfPage) ~> render(HomePage())

      val cacheTestPageRoute: dsl.Rule =
        staticRoute("#cacheTest", CacheTest_AbstrReprOfPage) ~>
          render({ cacheTestRootComp })


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

      println(s"page = ${r.page}")
    <.div(
      TopNavComp(TopNavComp.Props(mainMenu, r.page, c)),
      r.render(),
      FooterComp() //
    )
  }


}

