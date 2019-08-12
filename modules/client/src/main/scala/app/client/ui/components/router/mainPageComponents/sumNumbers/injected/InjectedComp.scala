package app.client.ui.components.router.mainPageComponents.sumNumbers.injected

import app.client.ui.caching.CacheInterface
import app.client.ui.caching.viewCache.ViewCacheStates
import app.client.ui.components.router.mainPageComponents.sumNumbers.injected.localState.{State, TheThieveryNumber}
import app.shared.dataModel.views.SumIntView
import app.shared.dataModel.views.SumIntView.SumIntView_Par
import bootstrap4.TB.C
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{BackendScope, Callback, CallbackTo, CtorType, ReactEventFromInput, ScalaComponent}
import org.scalajs.dom
import org.scalajs.dom.html.Input

private[sumNumbers] object InjectedComp {

//  private type State = OurState

  private[injected] var initialState = {
    val tn = TheThieveryNumber( 38, 45 )
    val siwp: SumIntView_Par = SumIntView_Par( 38, 45 )
    State( tn, siwp )
  }

  private[sumNumbers] val component: Component[
    CacheInterface,
    State,
    Backend,
    CtorType.Props
  ] = {
    ScalaComponent
      .builder[CacheInterface]( "TheCorporation" )
      .initialState( initialState )
      .renderBackend[Backend] // ← Use Backend class and backend.render
      .build
  }


}





