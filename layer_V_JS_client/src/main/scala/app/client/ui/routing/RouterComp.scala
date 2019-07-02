package app.client.ui.routing

import app.client.ui.routing.cache.exposed.ReRenderTriggererHolderSingletonGloballyAccessibleObject.ReRenderTriggerer
import app.client.ui.routing.cache.exposed.{
  CacheInterface,
  ReRenderTriggererHolderSingletonGloballyAccessibleObject
}
import app.client.ui.routing.canBeRoutedTo.DataRepresentations._
import app.client.ui.routing.canBeRoutedTo.components.cacheTestCompAndRelatedStuff.{
  CacheTestComp,
  CacheTestRootCompProps,
  NotWrapped_CacheTestRootComp_Backend
}
import app.client.ui.routing.canBeRoutedTo.components.HomePage
import app.client.ui.routing.generalComponents.TopNavComp.Menu
import app.client.ui.routing.generalComponents.{FooterComp, TopNavComp}
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.component.builder.Lifecycle
import japgolly.scalajs.react.extra.router.{
  Resolution,
  RouterConfigDsl,
  RouterCtl,
  _
}
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{CtorType, _}

// this wrapper is needed so that we can "re render the react tree below this"

class Wrapper(
    toBeWrapped: Component[CacheTestRootCompProps,
                           Unit,
                           NotWrapped_CacheTestRootComp_Backend,
                           CtorType.Props]) {

  lazy val wrapperConstructor =
    ScalaComponent
      .builder[CacheTestRootCompProps]( "Wrapper" )
      .renderBackend[WrapperBackend]
      .componentWillMount( $ => {
        println(
          "we are creating the callback that will be " +
            "executed when the wrapper component will mount"
        )
        Callback {

          println( "--------------- Our CALLBACK HAS BEEN CALLED !!!!" )
          val h: Lifecycle.ComponentWillMount[CacheTestRootCompProps,
                                              Unit,
                                              WrapperBackend] = $

          def f() = {
            val s = $.setState( Unit )
            println(
              s"we will run the callback that will set the state of the Wrapper Component " +
                s"to Unit, hopefully this should cause a re-render"
            )
            s.runNow()
          }

          val reRenderTriggerer: ReRenderTriggerer = ReRenderTriggerer( f )

          println(
            "we are now just about to mount the Wrapper component (as the direct child of the Router)," +
              "kind of as a bridge between the Router and the 'real deal', we need this bridge so that we can" +
              "trigger a re-render when the cache has been updated / freshed / 'filled-up' "
          )

          ReRenderTriggererHolderSingletonGloballyAccessibleObject
            .setTriggerer( reRenderTriggerer )
        }
      } )
      .build

//  lazy val compConstructor: Component[CacheTestRootCompProps, Unit, WrapperBackend, CtorType.Props] =
//    ScalaComponent
//      .builder[CacheTestRootCompProps]("WRAPPED CacheTestRootComp")
//      .renderBackend[WrapperBackend]
//      .build

  class WrapperBackend($ : BackendScope[CacheTestRootCompProps, Unit] ) {
    def render(props: CacheTestRootCompProps ) = {
      <.div( toBeWrapped( props ) )
    }
  }

}

case class RouterComp() {

  lazy val cache = new CacheInterface()

  val cacheTestRootComp =
    CacheTestComp.compConstructor

  val wrapped_cachTestRootComp = new Wrapper( cacheTestRootComp )

  val config = RouterConfigDsl[AbstrReprOfPage].buildConfig { dsl =>
    import dsl._

    val wr =
      wrapped_cachTestRootComp.wrapperConstructor(
        CacheTestRootCompProps( "These are the props via the wrapper",
                               cacheInterface = cache )
      )

    val homeRoute
        : dsl.Rule = staticRoute( root, HomePage_AbstrReprOfPage ) ~> render(
      HomePage()
    )

    val cacheTestPageRoute: dsl.Rule =
      staticRoute( "#cacheTest", CacheTest_AbstrReprOfPage ) ~>
        render( {
          wr
        } )

    (trimSlashes
      | homeRoute
      | cacheTestPageRoute)
      .notFound(
        redirectToPage( HomePage_AbstrReprOfPage )( Redirect.Replace )
      )
      .renderWith( layout )
  }

  val mainMenu = Vector(
    Menu( "Home", HomePage_AbstrReprOfPage ),
    Menu( "CacheTest", CacheTest_AbstrReprOfPage )
  )

  val baseUrl = BaseUrl.fromWindowOrigin_/

  val router =
    Router( baseUrl, config )

  def layout(c: RouterCtl[AbstrReprOfPage], r: Resolution[AbstrReprOfPage] ) = {

    println( s"page = ${r.page}" )
    <.div(
      TopNavComp( TopNavComp.Props( mainMenu, r.page, c ) ),
      r.render(),
      FooterComp() //
    )
  }

}
