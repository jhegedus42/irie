package app.client.ui.components.sodium

import app.client.ui.caching.cacheInjector.Cache
import app.client.ui.components.MainPage
import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import japgolly.scalajs.react.{CtorType, _}
import sodium.{Cell, StreamSink}


  // based on this example :
  // https://japgolly.github.io/scalajs-react/#examples/todo

//  Label is based on :
  // https://github.com/SodiumFRP/sodium/blob/89f40da2f62aed75201a86868e489f1564d667f6/book/swidgets/java/swidgets/src/swidgets/SLabel.java





  case class SodiumButtom(name: String = "Button") {

    val streamSink = new StreamSink[Unit]()

    val getVDOM: Component[Unit, CtorType.Nullary] =
      ScalaFnComponent[Unit] { props: Unit =>
        <.div(
          <.button(name, ^.onClick --> Callback({
            println("I was pushed")
            streamSink.send(Unit)
          }))
        )
      }

  }

