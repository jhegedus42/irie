package app.client.ui.components.router.mainPageComp.cacheTestMPC

import app.client.ui.caching.CacheInterface
import app.client.ui.caching.localState.ClientStateEntity
import app.client.ui.caching.viewCache.{SumIntViewCache, ViewCacheStates}
import app.client.ui.components.router.mainPageComp
import app.client.ui.components.router.mainPageComp.cacheTestMPC
import app.shared.rest.views.viewsForDevelopingTheViewFramework.SumIntView_HolderObject
import app.shared.rest.views.viewsForDevelopingTheViewFramework.SumIntView_HolderObject.SumIntView_Par
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}
import japgolly.scalajs.react.{CtorType, _}
import org.scalajs.dom.html.Input

object AddTheThieveryNumbersUsingTheServer {

  type State = TheThieveryNumber

  private var initialState
  : mainPageComp.cacheTestMPC.AddTheThieveryNumbersUsingTheServer.State =
    TheThieveryNumber(0.38, 0.45)

  def saveStateIntoInitState(s: State): Unit = {
    initialState = s
  }

  val TheCorporation
  : Component[CacheInterface,
    cacheTestMPC.AddTheThieveryNumbersUsingTheServer.State,
    ThieveryUndergroundBackend,
    CtorType.Props] = {
    ScalaComponent
      .builder[CacheInterface]("TheCorporation")
      .initialState(initialState)
      .renderBackend[ThieveryUndergroundBackend] // ← Use Backend class and backend.render
      .build
  }

  case class TheThieveryNumber(firstNumber: Double, secondNumber: Double)
    extends ClientStateEntity {}

  def getLineBreaks(i: Int) =
    TagMod(List.fill(i)(<.br).toIterator.toTraversable.toVdomArray)

  class ThieveryUndergroundBackend(bs: BackendScope[CacheInterface, State]) {

    def updateState(s2s: State => State): CallbackTo[Unit] = {
      bs.modState(
        (s: State) => {
          val newState: State = s2s(s)
          saveStateIntoInitState(newState)
          newState
        }
      )

    }

    def onChangeFirstNumber(
                             bs: BackendScope[CacheInterface, State]
                           )(e: ReactEventFromInput
                           ): CallbackTo[Unit] = {
      val event: _root_.japgolly.scalajs.react.ReactEventFromInput = e
      println(event)
      val target: Input = event.target
      val newValue: Double = target.valueAsNumber
      updateState(s => s.copy(firstNumber = newValue))
    }

    def onChangeSecondNumber(bs: BackendScope[CacheInterface, State])
                            (e: ReactEventFromInput): CallbackTo[Unit] = {
      val event: _root_.japgolly.scalajs.react.ReactEventFromInput = e
      println(event)
      val target: Input = event.target
      val newValue: Double = target.valueAsNumber
      updateState(s => s.copy(secondNumber = newValue))
    }

    def getTheSum(): String = {
      val params: SumIntView_Par = SumIntView_Par(38, 45)
      val res: Option[
        ViewCacheStates.ViewCacheState[SumIntView_HolderObject.SumIntView]
        ] =
        SumIntViewCache.getSumIntView(params)
      res.toString()
    }

    def render(s: State): VdomElement =
      <.div(
        <.hr,
        <.h3(
          "Itt van a Thievery Number osszeado alkalmazas (USING THE SERVER)!"
        ),
        getLineBreaks(5),

        <.div(
          s"${s.firstNumber} " +
            s"${s.secondNumber} " +
            s"The thievery number, the corporation."
        ),

        <.div(
          s"The sum of the thievery numbers is : " +
            s"${s.firstNumber + s.secondNumber}"
        ),

        <.input.number(^.onChange ==> onChangeFirstNumber(bs),
          ^.value := s.firstNumber),

        <.input.number(^.onChange ==> onChangeSecondNumber(bs),
          ^.value := s.secondNumber),

        getLineBreaks(5),
        "Here is the sum of the Thievery Numbers (as Integers), calculated on the server:",
        <.br,

        getTheSum()
        //  TODO, write similar "adding logic as the local adding logic"
        //   but now using the server, as well

      )
  }

}
