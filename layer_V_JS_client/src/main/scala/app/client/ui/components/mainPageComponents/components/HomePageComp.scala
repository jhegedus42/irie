package app.client.ui.components.mainPageComponents.components

import scalacss.Defaults._
import scalacss.ScalaCssReact._

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object HomePageComp {

  object Style extends StyleSheet.Inline {
    import dsl._
    val content = style(textAlign.center,
                        fontSize(30.px),
                        minHeight(450.px),
                        paddingTop(40.px))
  }

  val component =
    ScalaComponent.builder
      .static("HomePage")(<.div(Style.content, "Scala js react template"))
      .build

  def apply() = component()
}
