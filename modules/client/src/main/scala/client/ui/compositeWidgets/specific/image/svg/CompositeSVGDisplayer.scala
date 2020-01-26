package client.ui.compositeWidgets.specific.image.svg

import client.cache.relationalOperations.CellOptionMonad.CellOption
import client.cache.relationalOperations.onDataModel.NoteOperations
import client.ui.compositeWidgets.general.CellOptionDisplayerWidget
import client.ui.compositeWidgets.specific.image.svg.CompositeSVGDisplayer.VisualLinkData
import shared.dataStorage.model.{Note, VisualHint}
import shared.dataStorage.relationalWrappers.TypedReferencedValue

case class CompositeSVGDisplayer(
  selectedNote: CellOption[TypedReferencedValue[Note]]) {

  import japgolly.scalajs.react.vdom.html_<^.{<, _}

  lazy val hintToThisNote: CellOption[VisualHint] =
    selectedNote.map(
      _.versionedEntityValue.valueWithoutVersion.visualHint
    )

  lazy val nextNote: CellOption[TypedReferencedValue[Note]] =
    NoteOperations.getNextNote(selectedNote)

  lazy val hintToNextNote: CellOption[VisualHint] =
    nextNote.map(
      _.versionedEntityValue.valueWithoutVersion.visualHint
    )

  lazy val visualLinkDataCellOption: CellOption[VisualLinkData] = {
    hintToThisNote.lift2(hintToNextNote)(VisualLinkData(_, _))
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

  lazy val visualLinkComponentsAsVDOM = {
    //    import japgolly.scalajs.react.vdom.html_<^.{<, VdomElement}

    <.div(
      CellOptionDisplayerWidget(hintToThisNote.co, {
        (h: VisualHint) =>
          <.div(
            <.br,
            "Visual hint to this Note:",
            <.br,
            VisualLinkAsSVGHelpers.hintToThisNoteAsSVG(h)
          )
      }).optDisplayer(),
      CellOptionDisplayerWidget(
        hintToNextNote.co, { (h: VisualHint) =>
          <.div(
            <.br,
            "Hint to next note (to be placed into this Note's image)",
            <.br,
            VisualLinkAsSVGHelpers.hintToNextNoteAsSVG(h)
          )
        }
      ).optDisplayer()
    )
  }

}

object CompositeSVGDisplayer {

  case class VisualLinkData(
    thisNote: VisualHint,
    nextNote: VisualHint)

}
