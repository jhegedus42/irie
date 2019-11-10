package client.slinky

import slinky.core.{Component, SyntheticEvent}
import slinky.core.annotations.react
import slinky.web.html._
import slinky.core.{Component, StatelessComponent, SyntheticEvent}

import scala.scalajs.js.Date
import org.scalajs.dom.{Element, Event, html}
import slinky.core.facade.ReactInstance
import slinky.web.ReactDOM //nodisplay

object TestCode {

  def getTodoApp(mountNode: Element): ReactInstance = {
    case class TodoItem(
      text: String,
      id:   Long)

    @react class TodoApp extends Component {
      type Props = Unit
      case class State(
        items: Seq[TodoItem],
        text:  String)

      override def initialState = State(Seq.empty, "")

      def handleChange(e: SyntheticEvent[html.Input, Event]): Unit = {
        val eventValue = e.target.value
        setState(_.copy(text = eventValue))
      }

      def handleSubmit(e: SyntheticEvent[html.Form, Event]): Unit = {
        e.preventDefault()

        if (state.text.nonEmpty) {
          val newItem = TodoItem(
            text = state.text,
            id   = Date.now().toLong
          )

          setState(prevState => {
            State(
              items = prevState.items :+ newItem,
              text  = ""
            )
          })
        }
      }

      override def render() = {
        div(
          h3("TODO"),
          TodoList(items = state.items),
          form(onSubmit := (handleSubmit(_)))(
            input(
              onChange := (handleChange(_)),
              value := state.text
            ),
            button(s"Add #${state.items.size + 1}")
          )
        )
      }
    }

    @react class TodoList extends StatelessComponent {
      case class Props(items: Seq[TodoItem])

      override def render() = {
        ul(
          props.items.map { item =>
            li(key := item.id.toString)(item.text)
          }
        )
      }
    }

    val res: ReactInstance = ReactDOM.render(TodoApp(), mountNode)
    res
  }
}
