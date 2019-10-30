package app.client.ui.components.sodium

import app.client.ui.caching.cacheInjector.ReRenderer
import app.client.ui.components.sodium.SodiumWidgets.ReRenderTriggerer
import app.shared.entity.entityValue.values.Note
import japgolly.scalajs.react.component.ScalaFn.Component
import japgolly.scalajs.react.{
  BackendScope,
  Callback,
  ScalaFnComponent
}
import japgolly.scalajs.react.vdom.html_<^.{<, ^}
import sodium.{Cell, CellLoop, Stream, StreamSink}
import japgolly.scalajs.react.vdom.html_<^.{
  <,
  TagMod,
  VdomElement,
  ^,
  _
}
import japgolly.scalajs.react.{
  BackendScope,
  Callback,
  CallbackTo,
  CtorType,
  ReactEventFromInput,
  ScalaComponent
}
import monocle.macros.syntax.lens._
import org.scalajs.dom
import org.scalajs.dom.html.{Button, Input}

case class STextArea(init: Option[Note]) {

//  var note:Option[Note]= init

//  streamIn.listen({
//    x=> note=x;
//      ReRenderer.triggerReRender()
//  });

  val streamOut: StreamSink[Option[Note]] =
    new StreamSink[Option[Note]]()

  class Backend($ : BackendScope[Unit, Option[Note]]) {

    def render(s: Option[Note]) = {

      if (s.isDefined) {

        def onChange(e: ReactEventFromInput): Callback = {
          println("callback called")
          val newValue: String = e.target.value
          val newNote:  Note   = s.get.lens(_.title).set(newValue)
          $.setState(Some(newNote)) >> Callback {
            streamOut.send(Some(newNote))} >> Callback { println(s"state is $newNote")}

        }

        def title = s.get.lens(_.title).get

        <.div(
          <.textarea(^.onChange ==> onChange, ^.value := title)
        )
      } else <.div("loading")
    }

  }

  val component = ScalaComponent
    .builder[Unit]("STextArea")
    .initialState(init)
    .renderBackend[Backend]
    .build

}
