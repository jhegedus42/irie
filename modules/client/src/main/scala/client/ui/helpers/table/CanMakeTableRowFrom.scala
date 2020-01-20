package client.ui.helpers.table

import japgolly.scalajs.react.vdom.TagOf
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import org.scalajs.dom.html.TableRow
import org.scalajs.dom.{html => *}
import shared.dataStorage.model.{User, Value}

trait CanMakeTableRowFrom[V <: Value[V]] {

  def getTableRow(v: V): TagOf[*.TableRow]

}

object CanMakeTableRowFrom {

  implicit val canMakeTableRowFrom
    : CanMakeTableRowFrom[User] =
    new CanMakeTableRowFrom[User] {

      override def getTableRow(u: User): TagOf[TableRow] =
        <.tr(
          <.td(u.favoriteNumber),
          <.td(u.name)
        )
    }
}
