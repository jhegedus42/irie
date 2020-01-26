package client.ui.compositeWidgets.specific.image.svg

import client.cache.relationalOperations.CellOptionMonad.CellOption
import client.cache.relationalOperations.onDataModel.NoteOperations
import client.ui.compositeWidgets.general.CellOptionDisplayerWidget
import client.ui.compositeWidgets.specific.image.svg.CompositeSVGDisplayer.VisualLinkData
import shared.dataStorage.model.{Note, HintForNote}
import shared.dataStorage.relationalWrappers.TypedReferencedValue

case class CompositeSVGDisplayer(
  selectedNote: CellOption[TypedReferencedValue[Note]]) {

  import japgolly.scalajs.react.vdom.html_<^.{<, _}

  lazy val hintForNote: CellOption[HintForNote] =
    selectedNote.map(
      _.versionedEntityValue.valueWithoutVersion.visualHint
    )

  lazy val nextNote: CellOption[TypedReferencedValue[Note]] =
    NoteOperations.getNextNote(selectedNote)

  lazy val hintForNextNote: CellOption[HintForNote] =
    nextNote.map(
      _.versionedEntityValue.valueWithoutVersion.visualHint
    )

  lazy val visualLinkDataCellOption: CellOption[VisualLinkData] = {
    hintForNote.lift2(hintForNextNote)(VisualLinkData(_, _))
  }

  lazy val visualLinkAsVDOM = {
    <.div(
      CellOptionDisplayerWidget(
        visualLinkDataCellOption.co, { (vld: VisualLinkData) =>
          <.div(
            <.br,
            "Visual Link to next Note:",
            <.br,
            VisualLinkAsSVGHelpers.visualLinkToNextNoteAsSVG(vld)
          )
        }
      ).optDisplayer()
    )

  }


}

object CompositeSVGDisplayer {

  case class VisualLinkData(
                             hintForThis: HintForNote,
                             hintForNext: HintForNote)

}
