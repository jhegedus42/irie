package client.ui.helpers.table

import client.ui.helpers.vdom.ComposingVDOM
import japgolly.scalajs.react.vdom.{TagOf, VdomElement, html_<^}
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.html.{Table, TableCell, TableRow}
import org.scalajs.dom.{html => *}

import scala.collection.immutable

object TableHelpers {

  def getRow(l: List[VdomElement]): TagOf[TableRow] = {
    val wrapped: immutable.Seq[TagOf[TableCell]] =
      l.map(<.td(_))
    val row: html_<^.TagMod =
      ComposingVDOM.concatenateVDOMElements(
        wrapped.toList
      )
    <.tr(row)
  }

  def getTable(rows: List[TagOf[TableRow]]): TagOf[*.Table] = {
    val concatenatedRows: html_<^.TagMod =
      ComposingVDOM.concatenateVDOMElements(rows)
    <.table(concatenatedRows)
  }

  def getTableFromVdomElements(
    l: List[List[VdomElement]]
  ): TagOf[*.Table] = {
    val rows = l.map(getRow).toList
    val t    = getTable(rows)
    t
  }

  def getTableFromValues[V](
    l: List[V],
    f: V => List[VdomElement]
  ): VdomTagOf[Table] = {
    val vl = l.map(f)
    val t  = getTableFromVdomElements(vl)
    t
  }

}
