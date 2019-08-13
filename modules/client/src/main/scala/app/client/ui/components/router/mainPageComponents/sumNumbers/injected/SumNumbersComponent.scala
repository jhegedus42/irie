package app.client.ui.components.router.mainPageComponents.sumNumbers.injected

import app.client.ui.caching.{CacheInterface, CacheInterfaceWrapper}
import app.client.ui.components.router.mainPageComponents.sumNumbers.injected.localState.{SumNumberState, TheThieveryNumber}
import app.shared.dataModel.views.SumIntView.SumIntView_Par
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.{CtorType, ScalaComponent}



object SumNumbersComponent {

//  private type State = OurState


  private[injected] var initialState = {
    val tn = TheThieveryNumber( 38, 45 )
    val siwp: SumIntView_Par = SumIntView_Par( 38, 45 )
    SumNumberState( tn, siwp )
  }

  val component: Component[
    CacheInterfaceWrapper[SumNumbersProps],
    SumNumberState,
    SumNumbersBackend[SumNumbersProps],
    CtorType.Props
  ] = {
    ScalaComponent
      .builder[CacheInterfaceWrapper[SumNumbersProps]]( "TheCorporation" )
      .initialState( initialState )
      .renderBackend[SumNumbersBackend[SumNumbersProps]] // ← Use Backend class and backend.render
      .build
  }


}





