package app.shared.data.model

import app.shared.data.model.Entity.{Entity, Value}
import app.shared.data.ref.Ref

case class Date(date: java.util.Date = java.util.Calendar.getInstance().getTime() )

//import enumeratum._

//b3beb0310c464f69ad37651ab49a1c42

case class User(name: String, password: String ) extends Entity

case class UserLineList(
    user:      Ref[User],
    name:      String,
    isPrivate: Boolean = false,
    lines:     List[Ref[LineWithQue]] = List())
    extends Entity

case class LineWithQue(line: LineText, que: Option[ImgQue] = None ) extends Entity {
  // que itt megmondja h. melyik ImgQue-hoz tartozik ez a line list element
  // az itt levo ImgQue csak azok kozul az ImgQue-k kozul kerelhet ki
  // akiknek a line field-je megegyezik a ennek az entity-nek a line fieldjevel
}

case class Tag(name: String ) extends Entity

case class LineText(title: String, text: String, tags: Set[Ref[Tag]] = Set()  ) extends Entity

case class ImgQue(rect1: Option[ImgQue.Rect], rect2: Option[ImgQue.Rect], img: ImgQue.Img ) extends Entity

// ket ImageQue-t az image alapjan hasonlitunk ossze

object ImgQue {

  case class Rect(x: Int, y: Int, w: Int, h: Int ) extends Value

  case class Img(hash: String ) extends Value
  // egy user-nek ketszer uaz az image nem lehet
  // mert akkor  ket line-hoz kapcsolodna uaz az image

}

case class SharedLines(lineLists: List[LineText] ) extends Entity
// this is singleton, for now
