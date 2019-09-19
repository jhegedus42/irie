package app.client.ui.components.router

/**
  *
  * The purpose of this component is to wrap the router,
  * so that we do not have to wrap every top page (routed)
  * component, just so that we can re-render them.
  *
  */
import app.shared.utils.macros.compilationTime.AppendCompilationTimeToString
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^.{<, _}

object RouterWrapper {

  val router = RouterComp().routerComp()

  private object ReRendering {

    case class ReRenderTriggerer(triggerReRender: () => Unit)

    private var reRenderer: Option[ReRenderTriggerer] = None

    def setReRenderer(triggerer: ReRenderTriggerer): Unit = {
      reRenderer = Some(triggerer)
    }

    def reRenderApp(): Unit = {
      if (reRenderer.nonEmpty) reRenderer.get.triggerReRender()
      else {
        println(
          "BIMMMM ! Long time inna rock it inna dance but still," +
            "we cannot re render stuff without having a re-renderer, so plz. come" +
            "back later and bring a nice re-renderer, chala lala lala laaa. BIMM !"
        )
      }
    }


    // todo-later - make the re-renderer here to be the "only" re-render
    //
    //   and replace the current, complicated re-rendering logic by a simpler
    //   one, which is based on this re-renderer
    //
    //   The problem is, that I tried this one, instead of the other one, and it
    //   "did not work", meaning, the returning AJAX calls were not able to trigger
    //   re-renders. WHY ? I don't know. This is something that might be worth
    //   looking into, after MVP-2 is "ready", or if the
    //   current re-rendering solution will be prohibitively cumbersome to
    //   use (due to too much unneccessary confusing extra logic/boilerplate).
    //
    //  and use it instead of the curent "re-renderer"
    //  this is also needed to change the menu-bar
    //  based on if the user is logged in or some other
    //  state ... perhaps the app can have several "sub apps"
    //  which have different functionalities and hence different
    //  top menus

  }


  def reRenderApp() : Unit = ReRendering.reRenderApp()

  @AppendCompilationTimeToString val compilationTime: String =
    "Compilation time - of RouterWrapper - was : "

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
        "Debug Info:",
        <.br,
        s"$compilationTime, $s"
      )

    }
  }

  val component =
    ScalaComponent
      .builder[Unit]("Router Wrapper")
      .initialState(State.NumberOfTimesReRenderHasBeenCalled(0))
      .renderBackend[RouterWrapperBackend[Unit]]
      .componentWillMount(
        $ =>
          Callback {
            ReRendering
              .setReRenderer(ReRendering.ReRenderTriggerer(() => {
                $.modState(_.inc()).runNow()
              }))
          }
      )
      .build
}
