package client.ui.compositeWidgets.specific

import client.cache.AJAXCalls
import client.ui.atomicWidgets.input.SButton

import japgolly.scalajs.react.vdom.html_<^.{<, _}
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement}


case class SaveDataOnServerButton(){

  lazy val textButton = ???
  lazy val btn= SButton("Save Data On Server",Some({
    () => {
      println("we gonna save the data on the server !")
      AJAXCalls.saveDataOnServer("test2")
      println("AJAX call for saving data on the server has been sent.")
    }
  }))

  lazy val vdom= <.div(
   btn.comp()
  )
}