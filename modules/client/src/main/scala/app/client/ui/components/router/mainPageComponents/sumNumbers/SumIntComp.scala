package app.client.ui.components.router.mainPageComponents.sumNumbers

import app.client.ui.caching.cacheInjector.{
  Cache,
  MainPageReactCompWrapper,
  ToBeWrappedMainPageComponent
}
import app.client.ui.components.router.mainPageComponents.sumNumbers.SumIntComp.{
  SumNumberState,
  SumNumbersProps
}
import app.client.ui.caching.cacheInjector.CacheAndProps
import app.client.ui.components.router.mainPageComponents.sumNumbers.SumIntComp.{
  SumNumberState,
  SumNumbersProps,
  TheThieveryNumber
}
import app.shared.comm.postRequests.SumIntRoute.SumIntPar
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.{CtorType, ScalaComponent}
import app.client.ui.caching.cache.CacheEntryStates
import app.client.ui.caching.cacheInjector.CacheAndProps
import app.client.ui.components.router.mainPageComponents.SumIntPage
import app.client.ui.components.router.mainPageComponents.sumNumbers.SumIntComp.SumNumberState
import app.shared.comm.postRequests.SumIntRoute
import app.shared.comm.postRequests.SumIntRoute.SumIntPar
import bootstrap4.TB.C
import io.circe.generic.auto._
import japgolly.scalajs.react.vdom.html_<^.{
  <,
  TagMod,
  VdomElement,
  ^,
  _
}
import japgolly.scalajs.react.{
  BackendScope,
  Callback,
  CallbackTo,
  ReactEventFromInput
}
import monocle.macros.syntax.lens._
import org.scalajs.dom
import org.scalajs.dom.html.Input

trait SumIntComp
    extends ToBeWrappedMainPageComponent[SumIntComp, SumIntPage] {
  override type Props   = SumNumbersProps
  override type Backend = SumNumbersBackend[Props]
  override type State   = SumNumberState

}

object SumIntComp {
  import app.shared.comm.postRequests.SumIntRoute.SumIntPar
  import monocle.macros.Lenses

  @Lenses
  case class SumNumberState(
    tn:             TheThieveryNumber,
    sumIntViewPars: SumIntPar)

  @Lenses
  private[sumNumbers] case class TheThieveryNumber(
    firstNumber:  Int,
    secondNumber: Int)

//  case class SumNumbersURLPayload(string: String)
  case class SumNumbersProps(string: String)

  def getWrappedReactCompConstructor(
    cacheInterface:       Cache,
    propsProvderFunction: () => SumNumbersProps
  ) = {
    val reactCompWrapper =
      MainPageReactCompWrapper[SumIntComp, SumIntPage](
        cache         = cacheInterface,
        propsProvider = propsProvderFunction,
        comp          = SumNumbersComponent.component
      )
    reactCompWrapper.wrappedConstructor
  }

}

class SumNumbersBackend[Props](
  $ : BackendScope[CacheAndProps[Props], SumNumberState]) {

  /**
    * This makes sure that the next time this component will be "created"/"instantiated
    * it will remember its state. In other words: the user does not have to re-enter
    * the two numbers he/she entered earlier.
    *
    * @param s
    */
  private def saveStateIntoInitState(s: SumNumberState): Unit = {
    SumNumbersComponent.initialState = s
  }

  private def isThieveryNumber(st: SumNumberState): Boolean = {
    (st.tn.firstNumber == 38 && st.tn.secondNumber == 45)
  }

  /**
    * Creates `i` newlines.
    * @param i number of newlines to be created.
    * @return `i` newlines.
    */
  private def br(i: Int): TagMod =
    TagMod(List.fill(i)(<.br).toIterator.toTraversable.toVdomArray)

  private def showLibaCombAlert: Callback =
    Callback({

      dom.window.alert("LibaComb !")
      // from https://scala-js.github.io/scala-js-dom/
      println("button clicked")
    })

  /**
    * Asks the cache for the sum of two numbers.
    * @param props
    * @param params
    * @return the sum as String
    */
  private def calculateSumOnServer(
    props:  CacheAndProps[Props],
    params: SumIntPar
  ): CacheEntryStates.CacheEntryState[SumIntRoute] = {

    props.cache.getPostReqResult[SumIntRoute](params)
  }

  object StateChangers {

    def updateState(
      s2s: SumNumberState => SumNumberState
    ): CallbackTo[Unit] = {
      $.modState(
        (s: SumNumberState) => {
          val newState: SumNumberState = s2s(s)
          saveStateIntoInitState(newState)
          newState
        }
      )

    }

    def refreshState() =
      updateState((s: SumNumberState) => {
        s.lens(_.sumIntViewPars)
          .set(
            SumIntPar(s.tn.firstNumber, s.tn.secondNumber)
          )
      })

    def onChangeSecondNumber(
      bs: BackendScope[CacheAndProps[Props], SumNumberState]
    )(e:  ReactEventFromInput
    ): CallbackTo[Unit] = {
      val event: _root_.japgolly.scalajs.react.ReactEventFromInput = e
      println(event)
      val target:           Input  = event.target
      val number_as_double: Double = target.valueAsNumber
      val newValue:         Int    = number_as_double.round.toInt
      updateState(
        s => s.copy(tn = s.tn.copy(secondNumber = newValue))
      )
    }

    def onChangeFirstNumber(
      bs: BackendScope[CacheAndProps[Props], SumNumberState]
    )(e:  ReactEventFromInput
    ): CallbackTo[Unit] = {
      val event: _root_.japgolly.scalajs.react.ReactEventFromInput = e
      println(event)
      val target:           Input  = event.target
      val number_as_double: Double = target.valueAsNumber
      val newValue:         Int    = number_as_double.round.toInt
      updateState(s => s.copy(tn = s.tn.copy(firstNumber = newValue)))

    }

  }

  val intro =
    <.div(
      <.h1("The GOMB !"),
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

  def numberFields(s: SumNumberState): VdomElement =
    <.div(
      <.hr,
      <.h1("Thievery Number Sum Calculator"),
      <.br,
      <.div(
        s"The sum of the Thievery Numbers (calculated on client) is : " +
          s"${s.tn.firstNumber + s.tn.secondNumber}"
      ),
      br(2),
      <.input.number(
        ^.onChange ==> StateChangers.onChangeFirstNumber($),
        ^.value := s.tn.firstNumber
      ),
      <.input.number(
        ^.onChange ==> StateChangers.onChangeSecondNumber($),
        ^.value := s.tn.secondNumber
      ),
      <.br,
      if (isThieveryNumber(s)) {
        "All Good !"
      } else {
        "WARNING !!! You are trying to add two incompatible numbers."
      },
      <.br
    )

  def render(
    props: CacheAndProps[Props],
    s:     SumNumberState
  ): VdomElement = {
    <.div(
      C.textCenter,
      intro,
      numberFields(s),
      br(2),
      "Here is the sum of the Thievery Numbers (as Integers), " +
        "calculated on the server:",
      br(2),
      calculateSumOnServer(props, s.sumIntViewPars).toOption
        .toString(),
      br(2),
      "Also, here we have a bootstrap button " +
        "(only active for thievery numbers ! ) :",
      <.br,
      addNumbersBootStrapButton(s),
      <.br,
      <.div(
        <.hr,
        <.h1("The props !"),
        "The props that are passed to this component is:",
        <.br,
        props.props.toString,
        <.br,
        "This can come also, for example, from the router, " +
          "such as UUID for a user, or something similar."
      )
    )

  }

  private def addNumbersBootStrapButton(s: SumNumberState) = {
    import bootstrap4.TB.convertableToTagOfExtensionMethods
    <.button.btn.btnPrimary(
      "Add two Thievery Numbers",
      C.active.when(isThieveryNumber(s)),
      ^.onClick --> StateChangers.refreshState()
    )
  }

}

object SumNumbersComponent {

  //  private type State = OurState

  private[sumNumbers] var initialState = {
    val tn = TheThieveryNumber(38, 45)
    val siwp: SumIntPar = SumIntPar(38, 45)
    SumNumberState(tn, siwp)
  }

  val component: Component[
    CacheAndProps[SumNumbersProps],
    SumNumberState,
    SumNumbersBackend[SumNumbersProps],
    CtorType.Props
  ] = {
    ScalaComponent
      .builder[CacheAndProps[SumNumbersProps]](
        "TheCorporation"
      )
      .initialState(initialState)
      .renderBackend[SumNumbersBackend[SumNumbersProps]] // ← Use Backend class and backend.render
      .build
  }

}
