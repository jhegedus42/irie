package client.ui.compositeWidgets.specific.image

import client.cache.relationalOperations.CellOptionMonad.CellOption
import client.cache.relationalOperations.NoteOperations
import client.cache.{Cache, CacheMap}
import client.sodium.core.{CellLoop, CellSink}
import client.ui.atomicWidgets.input.SButton
import client.ui.atomicWidgets.show.text.SWPreformattedText
import client.ui.atomicWidgets.templates.CellTemplate
import client.ui.compositeWidgets.general.{
  CellOptionDisplayerWidget,
  EntitySelectorWidget,
  EntityUpdaterButton,
  TextFieldUpdaterWidget
}
import client.ui.helpers.table.TableHelpers
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement, _}
import org.scalajs.dom.html.Div

import scala.concurrent.ExecutionContextExecutor
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement}
import shared.dataStorage.model.{ImageWithQue, Note}
import shared.dataStorage.relationalWrappers.TypedReferencedValue

case class ImagesWidget(
  val selectedNote: CellOption[TypedReferencedValue[Note]]) {

  val selectedImage =
    selectedNote.map(_.versionedEntityValue.valueWithoutVersion.img)

  def getComp = {

    def render: Unit => VdomElement = { _ =>
      <.div(
        <.hr,
        <.h2("Images"),
        <.br,
//        ImageUploaderWidget(selector.selectedEntity, imgCache)
//          .render(),
        ImageDisplayerWidget(selectedImage.co)
          .imageDisplayer(),
//        <.br,
//        s"Make the Selected Note Point to the Selected Image by pressing this Button:",
//        entityUpdaterButton.updaterButton.comp(),
        <.br,
        <.hr
      )
    }

    val rootComp =
      ScalaComponent
        .builder[Unit]("ImagesWidget")
        .render_P(render)
        .build

    rootComp

  }

//  val entityUpdaterButton = {
//
//    import monocle.macros.syntax.lens._
//    val updater: CellOption[ImageWithQue => ImageWithQue] = {
//      val f = (trvNote: TypedReferencedValue[Note]) => {
//        (i: ImageWithQue) =>
//          i.lens(_.referenceToNote).set(Some(trvNote.ref))
//      }
//
//      val res: CellOption[ImageWithQue => ImageWithQue] =
//        selectedNote.map(f)
//      res
//    }
//
//    // todo-now
//    //  make a general composite updater
//    //  val i get the Image that refers to the selected note
//    //  make it point to no note
//    //  make this point to the selected image
//
//
//    // use the following cell to update the image
//    // belonging to the currently selected note :
//
//    val updaterCMD =NoteOperations.getNoteImageUpdaterCompositeCommand( ???, ??? )
//
//
//    val r = EntityUpdaterButton[ImageWithQue](
//      selector.selectedEntity,
//      imgCache,
//      updater,
//      "Update Image"
//    )
//
//    r
//  }

}
