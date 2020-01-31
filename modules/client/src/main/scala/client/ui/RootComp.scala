package client.ui

import scala.concurrent.ExecutionContextExecutor
import bootstrap4.TB.C
import client.ui.compositeWidgets.specific.SaveDataOnServerButton
import client.ui.compositeWidgets.specific.image.ImagesForANote
import client.ui.compositeWidgets.specific.image.svg.VisualLinkAsSVGHelpers
import client.ui.compositeWidgets.specific.note.NotesWidget
import client.ui.compositeWidgets.specific.noteFolder.ImageSequenceTraversingWidget
import client.ui.navigation.{Pages, NavigatorComp}
import client.ui.wrappedReact.{
  Crop,
  ImgCropWidget,
  ReactCrop,
  TagsInput
}
import japgolly.scalajs.react.vdom.SvgTagOf
import shared.dataStorage.model.{
  HintForNote,
  ImgFileName,
  SizeInPercentage
}

object RootComp {

  lazy val adminPWDSHA1Hash="939fbd7fce4a4b362f05043500a9386983b92928"

  import japgolly.scalajs.react.ScalaComponent
  import japgolly.scalajs.react.vdom.html_<^.{<, _}
  import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement}

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue



  def getComp = {

    def render: Unit => VdomElement = { _ =>
      NavigatorComp.vdom

    }

    val rootComp =
      ScalaComponent
        .builder[Unit]("Hello")
        .render_P(render)
        .build

    rootComp
  }

  // todo-now - ListOfNotes

}
