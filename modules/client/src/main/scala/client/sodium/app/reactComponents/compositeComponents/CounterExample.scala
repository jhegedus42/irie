package client.sodium.app.reactComponents.compositeComponents

import client.cache.{Cache, CacheMap}
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

  lazy val button1 = SButton("Inc 5", () => println("Inc 5 was pressed"))
  lazy val button2 = SButton("Dec 2", () => println("Dec 2 was presssed"))

//  val buttonWithFunction1 = button1.getClick.map(_ => { (x: Int) => x + 5 })
//  val buttonWithFunction2 = button2.getClick.map(_ => { (x: Int) => x - 2 })

  val buttonWithFunction: Stream[Int => Int] = new StreamSink[Int => Int]()
//    buttonWithFunction1.orElse(buttonWithFunction2)

  val counterCellLoop = Transaction.apply[CellLoop[Int]](
    { _ =>
      lazy val afterUpdate: Stream[Int] =
        buttonWithFunction.snapshot(counterValue, { (f, c: Int) =>
          f(c)
        })

      lazy val counterValue: CellLoop[Int] = new CellLoop[Int]()

      counterValue.loop(afterUpdate.hold(0))

      counterValue
    }
  )

  lazy val counterComp: CellTemplate[Int] = CellTemplate[Int](counterCellLoop, {
    i =>
      <.div(s"value is: $i")
  })

  def getComp = {

    val render: Unit => VdomElement = { _ =>
      <.div(s"This is the counter example",
            <.br,
            counterComp.getComp(),
            button1.vdom(),
            button2.vdom())
    }

    val rootComp =
      ScalaComponent
        .builder[Unit]("Hello")
        .render_P(render)
        .build

    rootComp
  }

}
