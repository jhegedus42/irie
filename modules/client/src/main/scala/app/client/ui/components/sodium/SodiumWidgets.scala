package app.client.ui.components.sodium

import app.client.ui.caching.cacheInjector.{Cache, CacheAndPropsAndRouterCtrl, ReRenderer}
import app.client.ui.caching.cacheInjector.ReRenderer.ReRenderTriggerer
import app.client.ui.components.MainPage
import sodium.{Cell, StreamSink}
import japgolly.scalajs.react.{CtorType, _}
import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import javax.swing.event.DocumentEvent.EventType
import org.scalajs.dom.html.Div

object SodiumWidgets {

  // based on this example :
  // https://japgolly.github.io/scalajs-react/#examples/todo

//  Label is based on :
  // https://github.com/SodiumFRP/sodium/blob/89f40da2f62aed75201a86868e489f1564d667f6/book/swidgets/java/swidgets/src/swidgets/SLabel.java

  case class ReRenderTriggerer(f: Option[() => Unit]) {

    def trigger(): Unit = {
      if (f.isDefined) {
        val r: () => Unit = f.get
        r()
      }
    }
  }

  case class SodiumLabel(val c: Cell[String]) {

    def sampledValue = c.sample()

    var state = "initial_state"

    var stateSetter: ReRenderTriggerer = ReRenderTriggerer(None)

    val f = c.listen(x => {
      println(s"sodum label's cell is $x");
      state = x
      stateSetter.trigger()
    })


    val comp = ScalaComponent
      .builder[Unit]("SodiumLabel")
      .initialState("label")
      .renderBackend[Backend]
      .componentWillMount(f => {
        val g = () => f.setState("bla").runNow()
        Callback {
          val s = ReRenderTriggerer(Some(g))
          stateSetter = s
        }
      })
      .build

    class Backend($ : BackendScope[Unit, String]) {
      def render(s: String) = {
        <.div(
          state
        )
      }
    }
  }

//  trait SodiumNetworkHandler[EventType]
//  {
//    def handler:Stream[EventType] => ()
//  }

  trait HasCacheAndRouterCtrl {
    def getCache:      Cache
    def getRouterCtrl: RouterCtl[MainPage]
  }

//  case class SodiumLabelGeneral[StreamType](
//    val c:  Cell[StreamType],
//    toVDOM: (StreamType => VdomTagOf[Div])) {
//    def s = c.sample()
//    val f = c.listen(x => println(s"sodum label's cell is $x"))
//    private val comp = ScalaFnComponent[StreamType] {
//      props: StreamType =>
//        println("sodium label was re-rendered");
//        toVDOM(s)
//    }
//    def vdom = comp(s)
//  }

  case class SodiumButtom() {
    val sClickedSink = new StreamSink[Unit]

    val getVDOM: Component[Unit, CtorType.Nullary] = ScalaFnComponent[Unit] { props: Unit =>
      <.div(
        <.button("String", ^.onClick --> Callback({
          println("I was pushed")
          sClickedSink.send(Unit)
        }))
      )
    }

  }



  println("The button was created")

  // todo later - make a sodium button out of this
//  https://github.com/SodiumFRP/sodium/blob/89f40da2f62aed75201a86868e489f1564d667f6/book/swidgets/java/swidgets/src/swidgets/SButton.java

  //  SButton is like a Swing JButton,
  //  but it exports an FRP stream that
  //  fires when the button is clicked,
  //  through a public field declared like this:
  //    public Stream<Unit> sClicked;

}

//package swidgets

//import nz.sodium._
//import sodium.Operational
//import sodium.StreamSink
//import sodium.Transaction
//import javax.swing.JButton
//import javax.swing.SwingUtilities
//import java.awt.event.ActionEvent
//import java.awt.event.ActionListener
//
//

//class SButton(val label: String, val enabled: Nothing) extends JButton(label) {
//  val sClickedSink = new StreamSink[Nothing]
//  this.sClicked = sClickedSink

//  addActionListener(new ActionListener() {
//    override def actionPerformed(e: ActionEvent): Unit = {
//      sClickedSink.send(Unit.UNIT)
//    }
//  })

//  // Do it at the end of the transaction so it works with looped cells
//  Transaction.post(() => setEnabled(enabled.sample))
//  l = Operational.updates(enabled).listen((ena: Any) => {
//    def foo(ena: Any) = {
//      if (SwingUtilities.isEventDispatchThread) this.setEnabled(ena)
//      else SwingUtilities.invokeLater(() => {
//        def foo() = {
//          this.setEnabled(ena)
//        }
//
//        foo()
//      })
//    }
//
//    foo(ena)
//  })
//
//  def this(label: String) {
//    this(label, new Nothing(true))
//  }
//
//  final private var l = null
//  final var sClicked: Nothing = null
//
//  override def removeNotify(): Unit = {
//    l.unlisten
//    super.removeNotify()
//  }
//}
