package app.client.ui.caching

import app.client.ui.caching.ReRenderer.ReRenderTriggerer
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import app.client.ui.components.mainPageComponents.components.cacheTestMainPageComp.{CacheTest_RootComp_Props, NotWrapped_CacheTestRootComp_Backend}
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.component.builder.Lifecycle

class CacheInjectorHOC(
                        toBeWrapped: Component[CacheTest_RootComp_Props,
                          Unit,
                          NotWrapped_CacheTestRootComp_Backend,
                          CtorType.Props]) {

  lazy val wrapperConstructor =
    ScalaComponent
      .builder[CacheTest_RootComp_Props]("Wrapper")
      .renderBackend[WrapperBackend]
      .componentWillMount($ => {
        println(
          "we are creating the callback that will be " +
            "executed when the wrapper component will mount"
        )
        Callback {

          println("--------------- Our CALLBACK HAS BEEN CALLED !!!!")
          val h: Lifecycle.ComponentWillMount[CacheTest_RootComp_Props,
            Unit,
            WrapperBackend] = $

          def f() = {
            val s = $.setState(Unit)
            println(
              s"we will run the callback that will set the state of the Wrapper Component " +
                s"to Unit, hopefully this should cause a re-render"
            )
            s.runNow()
          }

          val reRenderTriggerer: ReRenderTriggerer = ReRenderTriggerer(f)

          println(
            "we are now just about to mount the Wrapper component (as the direct child of the Router)," +
              "kind of as a bridge between the Router and the 'real deal', we need this bridge so that we can" +
              "trigger a re-render when the cache has been updated / freshed / 'filled-up' "
          )

          ReRenderer
            .setTriggerer(reRenderTriggerer)
        }
      })
      .build


  class WrapperBackend($: BackendScope[CacheTest_RootComp_Props, Unit]) {
    def render(props: CacheTest_RootComp_Props) = {
      <.div(toBeWrapped(props))
    }
  }

}
