package app.client.ui.components.generalComponents

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object FooterComp {

  val component = ScalaComponent.builder
    .static( "Footer 42" )(
      <.footer(
        ^.textAlign.center,
        <.div( ^.borderBottom := "1px solid grey", ^.padding := "0px" ),
        <.p( ^.paddingTop := "5px", "Footer" )
      )
    )
    .build

  def apply() = component()
}