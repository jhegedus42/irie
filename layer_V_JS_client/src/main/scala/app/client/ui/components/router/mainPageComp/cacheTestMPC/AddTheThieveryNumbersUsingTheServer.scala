package app.client.ui.components.router.mainPageComp.cacheTestMPC

import app.client.ui.caching.CacheInterface
import app.client.ui.caching.viewCache.ViewCacheStates
import app.client.ui.components.router.mainPageComp.cacheTestMPC
import app.shared.rest.views.viewsForDevelopingTheViewFramework.SumIntView_HolderObject.{SumIntView, SumIntView_Par}
import bootstrap4.TB.C
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, ^, _}
import japgolly.scalajs.react.{CtorType, _}
import org.scalajs.dom
import monocle.macros.syntax.lens._
import io.circe.generic.auto._
import org.scalajs.dom.html.Input

object AddTheThieveryNumbersUsingTheServer {

  type State = OurState

  private var initialState = {
    val tn = TheThieveryNumber( 38, 45 )
    val siwp: SumIntView_Par = SumIntView_Par( 38, 45 )
    OurState( tn, siwp )
  }

  val TheCorporation: Component[
    CacheInterface,
    cacheTestMPC.AddTheThieveryNumbersUsingTheServer.State,
    ThieveryUndergroundBackend,
    CtorType.Props
  ] = {
    ScalaComponent
      .builder[CacheInterface]( "TheCorporation" )
      .initialState( initialState )
      .renderBackend[ThieveryUndergroundBackend] // ← Use Backend class and backend.render
      .build
  }

  /**
    * This makes sure that the next time this component will be "created"/"instantiated
    * it will remember its state. In other words: the user does not have to re-enter
    * the two numbers he/she entered earlier.
    *
    * @param s
    */
  def saveStateIntoInitState( s: State ): Unit = {
    initialState = s
  }

  /**
    * Creates `i` newlines.
    * @param i number of newlines to be created.
    * @return `i` newlines.
    */
  def br(i: Int ) =
    TagMod( List.fill( i )( <.br ).toIterator.toTraversable.toVdomArray )

  def isThieveryNumber( st: State ): Boolean = {
    (st.tn.firstNumber == 38 && st.tn.secondNumber == 45)
  }

  def showLibaCombAlert: Callback =
    Callback( {

      dom.window.alert( "LibaComb !" )
      // from https://scala-js.github.io/scala-js-dom/
      println( "button clicked" )
    } )

  /**
    * Asks the cache for the sum of two numbers.
    * @param props
    * @param params
    * @return
    */
  def calculateSumOnServer(
      props:  CacheInterface,
      params: SumIntView_Par
  ): String = {

    val res: ViewCacheStates.ViewCacheState[SumIntView] =
      props.readView[SumIntView]( params )

    res.toString()
  }

  class ThieveryUndergroundBackend( $ : BackendScope[CacheInterface, State] ) {

    object StateChangers {

      def updateState( s2s: State => State ): CallbackTo[Unit] = {
        $.modState(
          (s: State) => {
            val newState: State = s2s( s )
            saveStateIntoInitState( newState )
            newState
          }
        )

      }

      def refreshState() =
        updateState( s => {
          s.lens( _.sumIntViewPars ).set(
              SumIntView_Par( s.tn.firstNumber, s.tn.secondNumber )
            )
        } )

      def onChangeSecondNumber(
          bs: BackendScope[CacheInterface, State]
      )( e:   ReactEventFromInput ): CallbackTo[Unit] = {
        val event: _root_.japgolly.scalajs.react.ReactEventFromInput = e
        println( event )
        val target:           Input  = event.target
        val number_as_double: Double = target.valueAsNumber
        val newValue:         Int    = number_as_double.round.toInt
        updateState( s => s.copy( tn = s.tn.copy( secondNumber = newValue ) ) )
      }

      def onChangeFirstNumber(
          bs: BackendScope[CacheInterface, State]
      )( e:   ReactEventFromInput ): CallbackTo[Unit] = {
        val event: _root_.japgolly.scalajs.react.ReactEventFromInput = e
        println( event )
        val target:           Input  = event.target
        val number_as_double: Double = target.valueAsNumber
        val newValue:         Int    = number_as_double.round.toInt
        updateState( s => s.copy( tn = s.tn.copy( firstNumber = newValue ) ) )

      }

    }

    val intro =
      <.div(
        <.h1( "The GOMB !" ),
        <.br,
        <.br,
        "This is an ancient, 1000 year old Hungarian GOMB, that gives you a LibaComb, when megnyomod:",
        <.br,
        <.br,
        <.button(
          "GOMB !",
          ^.onClick --> showLibaCombAlert
        ),
        <.br
//        getLineBreaks( 2 ),
//        s"The only two known Thievery Numbers are the following (discovered by The Corporation):",
//        <.br,
//        "The smallest Thievery Number (so far discovered) is : ${s.tn.firstNumber}",
//        <.br,
//        "The largest Thievery Number (so far discovered) is : ${s.tn.secondNumber} ",
//        <.br,
//        "Note that there might be more than these two, so far discovered, Thievery Numbers.",
//        <.br,
//        "If you suspect that you have discovered a new (so far not yet discovered Thievery Number," +
//          "please call immediately your nearest Thievery Corporation. Your life might be in danger!",
//        getLineBreaks( 2 )
      )

    def numberFields( s: State ): VdomElement =
      <.div(
        <.hr,
        <.h1( "Thievery Number Sum Calculator" ),
        <.br,
        <.div(
          s"The sum of the Thievery Numbers (calculated on client) is : " +
            s"${s.tn.firstNumber + s.tn.secondNumber}"
        ),
        br( 2 ),
        <.input.number(
          ^.onChange ==> StateChangers.onChangeFirstNumber( $ ),
          ^.value := s.tn.firstNumber
        ),
        <.input.number(
          ^.onChange ==> StateChangers.onChangeSecondNumber( $ ),
          ^.value := s.tn.secondNumber
        ),
        <.br,
        if (isThieveryNumber( s )) { "All Good !" } else {
          "WARNING !!! You are trying to add two incompatible numbers."
        },
        <.br
      )

    def render( props: CacheInterface, s: State ): VdomElement = {
      <.div(
        C.textCenter,
        intro,
        numberFields( s ),
        br( 2 ),
        "Here is the sum of the Thievery Numbers (as Integers), calculated on the server:",
        br( 2 ),
        calculateSumOnServer( props, s.sumIntViewPars ),
        br( 2 ) ,
        "Also, here we have a bootstrap button (only active for thievery numbers ! ) :",
        <.br,
        addNumbersBootStrapButton(s),
        <.br
      )
    }

    private def addNumbersBootStrapButton(s: State) = {
      import bootstrap4.TB.convertableToTagOfExtensionMethods
      <.button.btn.btnPrimary(
        "Add two Thievery Numbers",
        C.active.when(isThieveryNumber(s)),
        ^.onClick --> StateChangers.refreshState()
      )
    }
  }

}
