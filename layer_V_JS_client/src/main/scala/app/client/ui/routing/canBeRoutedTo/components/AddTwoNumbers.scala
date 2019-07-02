package app.client.ui.routing.canBeRoutedTo.components

import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}
//import japgolly.scalajs.react.{BackendScope, ScalaComponent}
import japgolly.scalajs.react._

object ExampleHolder {
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
        Test.TodoApp()
      )
    // we need input
    // steal it from :https://github.com/shogowada/scalajs-reactjs/tree/master/example

  }
}

object Test {
  val TodoList = ScalaFnComponent[List[String]] { props =>
    def createItem(itemText: String ) = <.li( itemText )
    <.ul( props map createItem: _* )
  }

  case class State(items: List[String], text: String )

  class Backend($ : BackendScope[Unit, State] ) {
    def onChange(e: ReactEventFromInput ) = {
      val newValue = e.target.value
      $.modState( _.copy( text = newValue ) )
    }

    def handleSubmit(e: ReactEventFromInput ) =
      e.preventDefaultCB >>
        $.modState( s => State( s.items :+ s.text, "" ) )

    def render(state: State ) =
      <.div(
        <.h3( "TODO" ),
        TodoList( state.items ),
        <.form( ^.onSubmit ==> handleSubmit,
               <.input( ^.onChange ==> onChange, ^.value := state.text ),
               <.button( "Add #", state.items.length + 1 ) )
      )
  }

  val TodoApp = ScalaComponent
    .builder[Unit]( "TodoApp" )
    .initialState( State( Nil, "" ) )
    .renderBackend[Backend]
    .build
}
