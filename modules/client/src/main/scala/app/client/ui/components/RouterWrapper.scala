package app.client.ui.components

/**
  *
  * The purpose of this component is to wrap the router,
  * so that we do not have to wrap every top page (routed)
  * component, just so that we can re-render them.
  *
  */
import app.client.ui.caching.cacheInjector.CacheInterfaceWrapper
import app.client.ui.components.router.RouterComp
import app.client.ui.components.router.mainPageComponents.sumNumbers.SumNumbersPage.SumNumberState
import app.shared.utils.macros.compilationTime.AppendCompilationTimeToString
import bootstrap4.TB.C
import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.vdom.html_<^.{<, _}

object RouterWrapper {
  val router=RouterComp().router()

  @AppendCompilationTimeToString val compilationTime: String = "Compilation time was : "

  object State {
    case class NumberOfTimesReRenderHasBeenCalled(nr: Long) {
      def inc() = copy(nr = this.nr + 1)
    }
  }

  class RouterWrapperBackend[Unit](
    $ : BackendScope[Unit, State.NumberOfTimesReRenderHasBeenCalled]) {

    def incCounterAndReRender() = $.modState(_.inc())

    def render(
      p: Unit,
      s: State.NumberOfTimesReRenderHasBeenCalled
    ): VdomElement = {

      <.div(
        router,
        <.br,
        <.hr,
        <.br,
        s"Debug Info: $compilationTime, $s",
      )

    }
  }

  import bootstrap4.TB.C

  val component =
    ScalaComponent
      .builder[Unit]("Router Wrapper")
      .initialState(State.NumberOfTimesReRenderHasBeenCalled(0))
      .renderBackend[RouterWrapperBackend[Unit]]
      .build

  // todo-now-1 - add here some kind of "re render triggerer"
  //
  //  and use it instead of the curent "re-renderer"
  //  this is also needed to change the menu-bar
  //  based on if the user is logged in or some other
  //  state ... perhaps the app can have several "sub apps"
  //  which have different functionalities and hence different
  //  top menus


}
