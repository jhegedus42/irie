package client.ui.compositeWidgets

import client.ui.atomicWidgets.input.SButton
import client.ui.atomicWidgets.templates.CellTemplate
import client.sodium.core.{StreamSink, _}
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, _}

import scala.concurrent.ExecutionContextExecutor

case class CounterExample() {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

//  val buttonWithFunction: Stream[Int => Int] =
//    new StreamSink[Int => Int]()

  lazy val button1 =
    SButton("Inc 5", () => println("Inc 5 was pressed"))

  lazy val button2 =
    SButton("Dec 2", () => println("Dec 2 was presssed"))

  val buttonWithFunction1 = button1.getClick.map(_ => {
    (x: Int) => x + 5
  })

  val buttonWithFunction2 = button2.getClick.map(_ => {
    (x: Int) => x - 2
  })

  val b = buttonWithFunction1.orElse(buttonWithFunction2)

  val counterCellLoop = Transaction.apply[CellLoop[Int]](
    { _ =>
      lazy val afterUpdate: Stream[Int] =
        b.snapshot(counterValue, { (f, c: Int) =>
          f(c)
        })

      lazy val counterValue: CellLoop[Int] =
        new CellLoop[Int]()

      counterValue.loop(afterUpdate.hold(0))

      counterValue
    }
  )

  lazy val counterComp: CellTemplate[Int] =
    CellTemplate[Int](counterCellLoop, { i =>
      <.div(s"value is: $i")
    })

  def getComp = {

    val render: Unit => VdomElement = { _ =>
      <.div(<.p(s"This is the counter example:"),
            <.br,
            counterComp.comp(),
            button1.comp(),
            <.br,
            button2.comp())
    }

    val rootComp =
      ScalaComponent
        .builder[Unit]("Hello")
        .render_P(render)
        .build

    rootComp
  }

}
