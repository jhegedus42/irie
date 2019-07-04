package app
  .client.ui.routing.canBeRoutedTo.components.cacheTestCompAndRelatedStuff

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}
import org.scalajs.dom.html.Input

object AddTheThieveryNumbersUsingTheServer {
  type State = TheThieveryNumber
  val TheCorporation = ScalaComponent
    .builder[Unit]( "Example" )
    .initialState( TheThieveryNumber( 0.38, 0.45 ) )
    .renderBackend[Backend] // ← Use Backend class and backend.render
    .build

  case class TheThieveryNumber(firstNumber: Double, secondNumber: Double ) {

    def onChangeFirstNumber(
        bs: BackendScope[Unit, State]
      )(
        e: ReactEventFromInput
      ) = {
      val event: _root_.japgolly.scalajs.react.ReactEventFromInput = e
      println( event )
      val target:   Input = event.target
      val newValue: Double = target.valueAsNumber
      bs.modState( s => s.copy( firstNumber = newValue ) )
    }

    def onChangeSecondNumber(
        bs: BackendScope[Unit, State]
      )(
        e: ReactEventFromInput
      ) = {
      val event: _root_.japgolly.scalajs.react.ReactEventFromInput = e
      println( event )
      val target:   Input = event.target
      val newValue: Double = target.valueAsNumber
      bs.modState( s => s.copy( secondNumber = newValue ) )
    }

  }

  class Backend(bs: BackendScope[Unit, State] ) {

    def render(
        s: State
      ): VdomElement = // ← Accept props, state and/or propsChildren as argument
      <.div(
        <.hr,
        <.h3(
          "Itt van a Thievery Number osszeado alkalmazas (USING THE SERVER)!"
        ),
        <.br,
        <.br,
        <.br,
        <.br,
        <.br,
        <.div(
          s"${s.firstNumber} ${s.secondNumber} The thievery number, the corporation."
        ),
        <.div(
          s"The sum of the thievery numbers is : ${s.firstNumber + s.secondNumber}"
        ),
        <.input.number( ^.onChange ==> s.onChangeFirstNumber( bs ),
                       ^.value := s.firstNumber ),
        <.input.number( ^.onChange ==> s.onChangeSecondNumber( bs ),
                       ^.value := s.secondNumber ),
        <.br,
        <.br,
        <.hr,
        <.br,
        <.br,
        <.br,
        <.br,
        <.br,
        <.br,
        <.br,
        <.br
      )

  }
}
