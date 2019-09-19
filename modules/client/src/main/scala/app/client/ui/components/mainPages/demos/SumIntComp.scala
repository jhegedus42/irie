package app.client.ui.components.mainPages.demos

import app.client.ui.caching.cache.CacheEntryStates
import app.client.ui.caching.cacheInjector.{Cache, CacheAndProps, MainPageReactCompWrapper, ToBeWrappedMainPageComponent}
import app.client.ui.components.mainPages.demos.SumIntComp.StateAndProps.{SumNumberState, SumNumbersProps}
import app.client.ui.components.router.RouterComp.RoutingRule
import app.client.ui.components.{MainPage, SumIntPage}
import app.shared.comm.postRequests.SumIntRoute
import bootstrap4.TB.C
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.extra.router.RouterConfigDsl
import japgolly.scalajs.react.vdom.html_<^.{<, TagMod, VdomElement, ^, _}
import japgolly.scalajs.react.{BackendScope, Callback, CallbackTo, CtorType, ReactEventFromInput, ScalaComponent}
import monocle.macros.syntax.lens._
import org.scalajs.dom
import org.scalajs.dom.html.Input

trait SumIntComp
    extends ToBeWrappedMainPageComponent[SumIntComp, SumIntPage] {
  override type Props   = SumNumbersProps
  override type Backend = SumIntComp.Backend[Props]
  override type State   = SumNumberState

}

object SumIntComp {
  import app.shared.comm.postRequests.SumIntRoute.SumIntPar
  import monocle.macros.Lenses

  object StateAndProps {

    @Lenses
    case class SumNumberState(
      tn:             TheThieveryNumber,
      sumIntViewPars: SumIntPar) {}

    case class SumNumbersProps(string: String)

    @Lenses
    case class TheThieveryNumber(
      firstNumber:  Int,
      secondNumber: Int
    )

    var initialState = {
      val tn = TheThieveryNumber(38, 45)
      val siwp: SumIntPar = SumIntPar(38, 45)
      SumNumberState(tn, siwp)
    }
  }

  def getRoute(cache: Cache): RoutingRule = {
    dsl: RouterConfigDsl[MainPage] =>
      import dsl._
      dynamicRouteCT("#sumInt" / int.caseClass[SumIntPage]) ~> dynRender {
        sip: SumIntPage =>
          {
            MainPageReactCompWrapper[SumIntComp, SumIntPage](
              cache = cache,
              propsProvider = () =>
                SumNumbersProps(
                  s"hello world 42 and also, hello ${sip.number}!"
                ),
              comp = SumIntComp.component
            ).wrappedConstructor
          }
      }

  }

  //  private type State = OurState

  val component: Component[
    CacheAndProps[SumNumbersProps],
    SumNumberState,
    Backend[SumNumbersProps],
    CtorType.Props
  ] = {
    ScalaComponent
      .builder[CacheAndProps[SumNumbersProps]](
        "TheCorporation"
      )
      .initialState(StateAndProps.initialState)
      .renderBackend[Backend[SumNumbersProps]] // ← Use Backend class and backend.render
      .build
  }

  class Backend[Props](
    $ : BackendScope[CacheAndProps[Props], SumNumberState]) {

    /**
      * This makes sure that the next time this component will be "created"/"instantiated
      * it will remember its state. In other words: the user does not have to re-enter
      * the two numbers he/she entered earlier.
      *
      * @param s
      */
    private def saveStateIntoInitState(s: SumNumberState): Unit = {
      SumIntComp.StateAndProps.initialState = s
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
        val event: _root_.japgolly.scalajs.react.ReactEventFromInput =
          e
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
        val event: _root_.japgolly.scalajs.react.ReactEventFromInput =
          e
        println(event)
        val target:           Input  = event.target
        val number_as_double: Double = target.valueAsNumber
        val newValue:         Int    = number_as_double.round.toInt
        updateState(
          s => s.copy(tn = s.tn.copy(firstNumber = newValue))
        )

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
}
