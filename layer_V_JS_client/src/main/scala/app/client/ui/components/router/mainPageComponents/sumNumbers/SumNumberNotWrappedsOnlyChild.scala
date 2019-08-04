package app.client.ui.components.router.mainPageComponents.sumNumbers

import app.client.ui.caching.CacheInterface
import app.client.ui.caching.viewCache.ViewCacheStates
import app.shared.rest.views.viewsForDevelopingTheViewFramework.SumIntView_HolderObject.{SumIntView, SumIntView_Par}
import bootstrap4.TB.C
import io.circe.generic.auto._
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, ^, _}
import japgolly.scalajs.react.{CtorType, _}
import monocle.macros.Lenses
import monocle.macros.syntax.lens._
import org.scalajs.dom
import org.scalajs.dom.html.Input

@Lenses
private[sumNumbers] case class TheThieveryNumber(firstNumber: Int, secondNumber: Int )

@Lenses
private[sumNumbers] case class OurState(tn: TheThieveryNumber, sumIntViewPars: SumIntView_Par )


private[sumNumbers] object SumNumberCompInjected {

  private type State = OurState

  private var initialState = {
    val tn = TheThieveryNumber( 38, 45 )
    val siwp: SumIntView_Par = SumIntView_Par( 38, 45 )
    OurState( tn, siwp )
  }

  private[sumNumbers] val TheCorporation: Component[
    CacheInterface,
    SumNumberCompInjected.State,
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
  private def saveStateIntoInitState( s: State ): Unit = {
    initialState = s
  }

  /**
    * Creates `i` newlines.
    * @param i number of newlines to be created.
    * @return `i` newlines.
    */
  private def br(i: Int ) =
    TagMod( List.fill( i )( <.br ).toIterator.toTraversable.toVdomArray )

  private def isThieveryNumber( st: State ): Boolean = {
    (st.tn.firstNumber == 38 && st.tn.secondNumber == 45)
  }

  private def showLibaCombAlert: Callback =
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
  private def calculateSumOnServer(
      props:  CacheInterface,
      params: SumIntView_Par
  ): String = {

    val res: ViewCacheStates.ViewCacheState[SumIntView] =
      props.readView[SumIntView]( params )

    import app.shared.data.utils.PrettyPrint.prettyPrint

    prettyPrint(res,indentSize = 4)
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
        val res: String = calculateSumOnServer( props, s.sumIntViewPars );

      <.div(
        C.textCenter,
        intro,
        numberFields( s ),
        br( 2 ),
        "Here is the sum of the Thievery Numbers (as Integers), calculated on the server:",
        br( 2 ),
          res,
        br( 2 ),
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
