package client.sodium.app.reactComponents.compositeComponents

import client.cache.{Cache, CacheMap}
import client.sodium.app.actions.SActionWriteToConsole
import client.sodium.app.reactComponents.atomicComponents.{
  CellPreformattedText,
  CellTemplate,
  SButton,
  SPreformattedText,
  STextArea
}
import client.sodium.{CellLoopWithUpdaterStream, core}
import client.sodium.core._
import dataStorage.{ReferencedValue, User}
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement}
import japgolly.scalajs.react.vdom.html_<^.{<, _}

import scala.concurrent.ExecutionContextExecutor

case class TodoList() {

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  lazy val initTodo = List("pet cat", "go for a walk")

  lazy val todoLoop = CellLoopWithUpdaterStream[List[String]](initTodo)

//  val todos = new Cell[List[String]](initTodo)

  def getPrettyPrintedString(l: List[String]): String = {
    l.foldLeft("")((s, v) => s + s" ${v} \n")
  }

  def getComp = {

//    lazy val ilist: String = getPrettyPrintedString(todoLoop.cellLoop.sample())

    lazy val listOfTodos = CellPreformattedText(
      todoLoop.cellLoop.map(getPrettyPrintedString)
    ).comp

    val nrOfTodos = CellPreformattedText(
      todoLoop.cellLoop
        .map(c => s"number of todos : ${c.size.toString}")
    ).comp

    val todoElementName: STextArea = STextArea("buy beer")

    //    def inserter : StreamSink

    val addTodoElementButton =
      SButton(
        "Add New Todo",
        () => {
          val td = todoElementName.cell.sample()

          println(s"i was pusssshed, todo text is $td")
          val transformer: List[String] => List[String] = { (l: List[String]) =>
            td :: l
          }
          val startList       = List("egy")
          val transFormedList = transformer(startList)
          println(s"""
                     |
                     | startList:
                     | $startList
                     | 
                     | transFormedList:
                     | $transFormedList
                     |
                     |""".stripMargin)
          todoLoop.updaterStream.send(transformer)
        }
      )

    def render: Unit => VdomElement = { _ =>
      <.div(
        listOfTodos(),
        todoElementName.comp(),
        addTodoElementButton.vdom(),
        <.br,
        nrOfTodos()
      )
    }

    val compToReturn =
      ScalaComponent
        .builder[Unit]("Hello")
        .render_P(render)
        .build

    compToReturn

  }

}
