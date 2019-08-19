package app.client.ui.components.router.mainPageComponents.sumNumbers

import app.client.ui.caching.postRequestResultCache.PostRequestResultCacheEntryStates
import app.client.ui.caching.cacheInjector.CacheInterfaceWrapper
import app.client.ui.components.router.mainPageComponents.sumNumbers.data.SumNumberState
import app.shared.dataModel.views.SumIntPostRequest
import app.shared.dataModel.views.SumIntPostRequest.SumIntView_Par
import bootstrap4.TB.C
import io.circe.generic.auto._
import japgolly.scalajs.react.vdom.html_<^.{<, TagMod, VdomElement, ^, _}
import japgolly.scalajs.react.{
  BackendScope,
  Callback,
  CallbackTo,
  ReactEventFromInput
}
import monocle.macros.syntax.lens._
import org.scalajs.dom
import org.scalajs.dom.html.Input

class SumNumbersBackend[Props](
    $ : BackendScope[CacheInterfaceWrapper[Props], SumNumberState]
) {

  /**
    * This makes sure that the next time this component will be "created"/"instantiated
    * it will remember its state. In other words: the user does not have to re-enter
    * the two numbers he/she entered earlier.
    *
    * @param s
    */
  private def saveStateIntoInitState( s: SumNumberState ): Unit = {
    SumNumbersComponent.initialState = s
  }
  private def isThieveryNumber( st: SumNumberState ): Boolean = {
    (st.tn.firstNumber == 38 && st.tn.secondNumber == 45)
  }

  /**
    * Creates `i` newlines.
    * @param i number of newlines to be created.
    * @return `i` newlines.
    */
  private def br( i: Int ): TagMod =
    TagMod( List.fill( i )( <.br ).toIterator.toTraversable.toVdomArray )

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
    * @return the sum as String
    */
  private def calculateSumOnServer(
      props:  CacheInterfaceWrapper[Props],
      params: SumIntView_Par
  ): PostRequestResultCacheEntryStates.PostRequestResultCacheEntryState[SumIntPostRequest] = {

    props.cacheInterface.readView[SumIntPostRequest]( params )
  }

  object StateChangers {

    def updateState(
        s2s: SumNumberState => SumNumberState
    ): CallbackTo[Unit] = {
      $.modState(
        (s: SumNumberState) => {
          val newState: SumNumberState = s2s( s )
          saveStateIntoInitState( newState )
          newState
        }
      )

    }

    def refreshState() =
      updateState( (s: SumNumberState) => {
        s.lens( _.sumIntViewPars ).set(
            SumIntView_Par( s.tn.firstNumber, s.tn.secondNumber )
          )
      } )

    def onChangeSecondNumber(
        bs: BackendScope[CacheInterfaceWrapper[Props], SumNumberState]
    )( e:   ReactEventFromInput ): CallbackTo[Unit] = {
      val event: _root_.japgolly.scalajs.react.ReactEventFromInput = e
      println( event )
      val target:           Input  = event.target
      val number_as_double: Double = target.valueAsNumber
      val newValue:         Int    = number_as_double.round.toInt
      updateState( s => s.copy( tn = s.tn.copy( secondNumber = newValue ) ) )
    }

    def onChangeFirstNumber(
        bs: BackendScope[CacheInterfaceWrapper[Props], SumNumberState]
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

  def numberFields( s: SumNumberState ): VdomElement =
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

  def render(
      props: CacheInterfaceWrapper[Props],
      s:     SumNumberState
  ): VdomElement = {
    <.div(
      C.textCenter,
      intro,
      numberFields( s ),
      br( 2 ),
      "Here is the sum of the Thievery Numbers (as Integers), calculated on the server:",
      br( 2 ),
      calculateSumOnServer( props, s.sumIntViewPars ).toOption.toString(),
      br( 2 ),
      "Also, here we have a bootstrap button (only active for thievery numbers ! ) :",
      <.br,
      addNumbersBootStrapButton( s ),
      <.br,
      "The props that are passed to this component is:",
      <.br,
      props.props.toString
    )

  }

  private def addNumbersBootStrapButton( s: SumNumberState ) = {
    import bootstrap4.TB.convertableToTagOfExtensionMethods
    <.button.btn.btnPrimary(
      "Add two Thievery Numbers",
      C.active.when( isThieveryNumber( s ) ),
      ^.onClick --> StateChangers.refreshState()
    )
  }
}
