package client.sodium.app.reactComponents.compositeComponents

import client.cache.{Cache, CacheMap, NormalizedStateHolder}
import client.sodium.app.actions.SActionWriteToConsole
import client.sodium.app.reactComponents.atomicComponents.{
  CellTemplate,
  SButton,
  SPreformattedText,
  STextArea,
  StreamTemplate
}
import client.sodium.core.StreamSink
import client.sodium.core._
import dataStorage.User
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, _}

import scala.concurrent.ExecutionContextExecutor

case class CounterExample() {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  lazy val button = SButton("Inc")

  val counter_ = Transaction.apply[CellTemplate[Int]]({ _ =>
    lazy val afterUpdate: Stream[Int] = button.getClick.snapshot(counterValue, {
      (_, c: Int) =>
        c + 1
    })

    lazy val counterValue: CellLoop[Int] = new CellLoop[Int]()

    counterValue.loop(afterUpdate.hold(0))

    lazy val counter: CellTemplate[Int] = CellTemplate[Int](counterValue, { i =>
      <.div(s"value is: $i")
    })
    counter
  })

  def getComp = {

    val render: Unit => VdomElement = { _ =>
      <.div(s"This is the counter example",
            <.br,
            counter_.getComp(),
            button.vdom())
    }

    val rootComp =
      ScalaComponent
        .builder[Unit]("Hello")
        .render_P(render)
        .build

    rootComp
  }

}
