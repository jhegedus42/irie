package client.ui.compositeWidgets.specific

import client.cache.AJAXCalls
import client.ui.atomicWidgets.input.{SButton, STextArea}
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement}

case class SaveDataOnServerButton() {

  val pwdField = STextArea("")

  lazy val btn = SButton(
    "Save Data On Server",
    Some({ () =>
      {
        println("we gonna save the data on the server !")
//        AJAXCalls.saveDataOnServer(pwdField.cell.sample())
        println(
          "AJAX call for saving data on the server has been sent."
        )
      }
    })
  )

  lazy val vdom = <.div(
    "Password",
    <.br,
    pwdField.comp(),
    <.br,
    btn.comp()
  )
}
