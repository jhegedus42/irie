package app.client.ui.components.router.mainPageComp

import scalacss.Defaults._
import scalacss.ScalaCssReact._
import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.vdom.html_<^._

object HomeMPC {

  object Style extends StyleSheet.Inline {
    import dsl._
    val content: StyleA = style.apply( textAlign.center,
                                      fontSize( 30.px ),
                                      minHeight( 450.px ),
                                      paddingTop( 40.px ) )
  }

  private val component =
    ScalaComponent.builder
      .static( "HomePage" )(
        <.div( Style.content, "Scala js react template, SBT 1.2.8" )
      )
      .build

  def apply(): Unmounted[Unit, Unit, Unit] = component()
}
