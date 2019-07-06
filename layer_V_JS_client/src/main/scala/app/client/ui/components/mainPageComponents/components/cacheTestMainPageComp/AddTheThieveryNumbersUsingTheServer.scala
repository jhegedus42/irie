package app
  .client.ui.components.mainPageComponents.components.cacheTestMainPageComp

import app.client.ui.caching.viewCache.SumIntViewCache
import app.client.ui.components.mainPageComponents.components.cacheTestMainPageComp
import app.shared.rest.views.viewsForDevelopingTheViewFramework.SumIntView_HolderObject
import app.shared.rest.views.viewsForDevelopingTheViewFramework.SumIntView_HolderObject.SumIntView_Par
import japgolly.scalajs.react.{CtorType, _}
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}
import org.scalajs.dom.html.Input

object AddTheThieveryNumbersUsingTheServer {

  type State = TheThieveryNumber

  val TheCorporation
      : Component[Unit,
                  TheThieveryNumber,
                  ThieveryUndergroundBackend,
                  CtorType.Nullary] = ScalaComponent.builder[Unit]( "Example" )
      .initialState( TheThieveryNumber( 0.38, 0.45 ) )
      .renderBackend[ThieveryUndergroundBackend] // ← Use Backend class and backend.render
      .build

  case class TheThieveryNumber(firstNumber: Double, secondNumber: Double ) {

    def onChangeFirstNumber( bs: BackendScope[Unit, State] )( e: ReactEventFromInput ) = {
      val event: _root_.japgolly.scalajs.react.ReactEventFromInput = e
      println( event )
      val target:   Input = event.target
      val newValue: Double = target.valueAsNumber
      bs.modState( s => s.copy( firstNumber = newValue ) )
    }

    def onChangeSecondNumber( bs: BackendScope[Unit, State] )( e: ReactEventFromInput ) = { val event: _root_.japgolly.scalajs.react.ReactEventFromInput = e
      println( event )
      val target:   Input = event.target
      val newValue: Double = target.valueAsNumber
      bs.modState( s => s.copy( secondNumber = newValue ) )}}

  def getLineBreaks(i: Int ) = TagMod( List.fill( i )( <.br ).toIterator.toTraversable.toVdomArray )

  class ThieveryUndergroundBackend(bs: BackendScope[Unit, State] ) {

    def render( s: State ): VdomElement =
      <.div(
        <.hr,

        <.h3( "Itt van a Thievery Number osszeado alkalmazas (USING THE SERVER)!" ),

        getLineBreaks(5),

        <.div(  s"${s.firstNumber} " +
                s"${s.secondNumber} " +
                s"The thievery number, the corporation." ),

        <.div( s"The sum of the thievery numbers is : " +
               s"${s.firstNumber + s.secondNumber}" ),

        <.input.number( ^.onChange ==> s.onChangeFirstNumber( bs ),
                        ^.value := s.firstNumber ),
        <.input.number( ^.onChange ==> s.onChangeSecondNumber( bs ),
                        ^.value := s.secondNumber ),

        getLineBreaks(5),

        "Here is the sum of the Thievery Numbers (as Integers), calculated on the server:",

        <.br,

        {
          val params: SumIntView_Par = SumIntView_Par( 38, 45 )
          val res: Option[SumIntView_HolderObject.SumIntView_Res] =
            SumIntViewCache.getSumIntView( params )
          res.toString()
        }

      )
  }
}
