package app
  .client.ui.routing.canBeRoutedTo.components.cacheTestCompAndRelatedStuff

import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}
import japgolly.scalajs.react._
import org.scalajs.dom.html.Input

object AddTheThieveryNumbers {
  type State = TheThieveryNumber
  val TheCorporation = ScalaComponent
    .builder[Unit]( "Example" )
    .initialState( TheThieveryNumber( 0.38, 0.45 ) )
    .renderBackend[Backend] // ← Use Backend class and backend.render
    .build

  case class TheThieveryNumber(firstNumber: Double, b: Double ) {

    def onChangeFirstNumber(
        bs: BackendScope[Unit, State]
      )(
        e: ReactEventFromInput
      ) = {

      val event: _root_.japgolly.scalajs.react.ReactEventFromInput = e
      println( event )

      val target: Input = event.target

      val newValue: Double = target.valueAsNumber

      bs.modState( s => s.copy( firstNumber = newValue ) )
    }

  }

  class Backend(bs: BackendScope[Unit, State] ) {

    def render(
        s: State
      ): VdomElement = // ← Accept props, state and/or propsChildren as argument
      <.div(
        <.div(
          s"${s.firstNumber} ${s.b} The thievery number, the corporation."
        ),
        <.div( s"The sum of the thievery numbers is : ${s.firstNumber + s.b}" ),
        <.div( s"gyakorlaskeppen ide teszunk meg egy TODO appot :" ),
        <.input( ^.onChange ==> s.onChangeFirstNumber( bs ),
                ^.value := s.firstNumber ),
        TodoListExample.TodoApp()
      )
    // we need input
    // steal it from :https://github.com/shogowada/scalajs-reactjs/tree/master/example

  }
}
