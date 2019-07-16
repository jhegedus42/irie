package app.client.ui.components.router.mainPageComp.cacheTestMPC

import app.client.ui.caching.CacheInterface
import app.client.ui.caching.viewCache.{SumIntViewCache, ViewCacheStates}
import app.client.ui.components.router.mainPageComp.cacheTestMPC
import app.shared.rest.views.viewsForDevelopingTheViewFramework.SumIntView_HolderObject
import app.shared.rest.views.viewsForDevelopingTheViewFramework.SumIntView_HolderObject.SumIntView_Par
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, ^, _}
import japgolly.scalajs.react.{CtorType, _}
//import monocle.macros.Lenses
import monocle.macros.syntax.lens._
//import org.scalajs.dom
import org.scalajs.dom.html.Input


object AddTheThieveryNumbersUsingTheServer {


  type State = OurState

  private var initialState = {
    val tn = TheThieveryNumber(38, 45)
    val siwp: SumIntView_Par = SumIntView_Par(38, 45)
    OurState(tn, siwp)
  }

  //noinspection UnitMethodDefinedLikeFunction
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
      val number_as_double: Double = target.valueAsNumber
      val newValue: Int = number_as_double.round.toInt
      updateState(s => s.copy(tn = s.tn.copy(firstNumber = newValue)))

    }

    def onChangeSecondNumber(
                              bs: BackendScope[CacheInterface, State]
                            )(e: ReactEventFromInput
                            ): CallbackTo[Unit] = {
      val event: _root_.japgolly.scalajs.react.ReactEventFromInput = e
      println(event)
      val target: Input = event.target
      val number_as_double: Double = target.valueAsNumber
      val newValue: Int = number_as_double.round.toInt
      updateState(s => s.copy(tn = s.tn.copy(secondNumber = newValue)))
    }

    def getTheSum(params: SumIntView_Par): String = {
      val res: Option[
        ViewCacheStates.ViewCacheState[SumIntView_HolderObject.SumIntView]
        ] =
        SumIntViewCache.getSumIntView(params)
      res.toString()
    }

    def buttonClicked(bs: BackendScope[CacheInterface, State]): Callback =
//      Callback({
//
//
//        dom.window.alert("libacombot !")
//        // from https://scala-js.github.io/scala-js-dom/
//        println("button clicked")
//      }) >>
        updateState(s => {
        s.lens(_.sumIntViewPars).set(SumIntView_Par(s.tn.firstNumber, s.tn.secondNumber))
      })


    def render(s: State): VdomElement =
      <.div(
        <.hr,
        <.h3(
          "Itt van a Thievery Number osszeado alkalmazas (USING THE SERVER)!"
        ),
        getLineBreaks(5),
        <.div(
          s"${s.tn.firstNumber} " +
            s"${s.tn.secondNumber} " +
            s"The thievery number, the corporation."
        ),
        <.div(
          s"The sum of the thievery numbers is : " +
            s"${s.tn.firstNumber + s.tn.secondNumber}"
        ),
        <.input.number(^.onChange ==> onChangeFirstNumber(bs),
          ^.value := s.tn.firstNumber),
        <.input.number(^.onChange ==> onChangeSecondNumber(bs),
          ^.value := s.tn.secondNumber),
        getLineBreaks(5),
        "Here is the sum of the Thievery Numbers (as Integers), calculated on the server:",
        <.br,

        getTheSum(s.sumIntViewPars),

        <.button(
          "ha megnyomod a gombot, kapsz egy ?",
          ^.onClick ==> { (_: ^.onClick.Event) => buttonClicked(bs) }
        ),

        getTheSum(s.sumIntViewPars),
        <.br,
        "---- itt a mese vege ----",
        <.br
      )
  }

}
