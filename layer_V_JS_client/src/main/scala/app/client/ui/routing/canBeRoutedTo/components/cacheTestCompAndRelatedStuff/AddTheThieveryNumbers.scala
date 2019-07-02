package app
  .client.ui.routing.canBeRoutedTo.components.cacheTestCompAndRelatedStuff

import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}
import japgolly.scalajs.react._

object AddTheThieveryNumbers {
  case class TheThieveryNumber(a: Double, b: Double )
  type State = TheThieveryNumber
  val TheCorporation = ScalaComponent
    .builder[Unit]( "Example" )
    .initialState( TheThieveryNumber( 0.38, 0.45 ) )
    .renderBackend[Backend] // ← Use Backend class and backend.render
    .build

  class Backend(bs: BackendScope[Unit, State] ) {
    def render(
        s: State
      ): VdomElement = // ← Accept props, state and/or propsChildren as argument
      <.div(
        <.div( s"${s.a} ${s.b} The thievery number, the corporation." ),
        <.div( s"The sum of the thievery numbers is : ${s.a + s.b}" ),
        <.div( s"gyakorlaskeppen ide teszunk meg egy TODO appot :" ),
        TodoListExample.TodoApp()
      )
    // we need input
    // steal it from :https://github.com/shogowada/scalajs-reactjs/tree/master/example

  }
}
