package app.shared.utils

import scalaz.\/

import scala.util.matching.Regex

/**
  * Created by joco on 28/04/2017.
  */
object UUID_Utils {

  case class EntityUUID(id: String=java.util.UUID.randomUUID().toString){
//    def isCorrect: Boolean = UUID.validate_from_String(id).isRight
  }

  object EntityUUID {
//    def random(): EntityUUID = EntityUUID()
//    implicit val _from_string: String => EntityUUID = s => EntityUUID(s)
//    implicit val _to_string: EntityUUID => String = (u: EntityUUID) => u.id
//    implicit val _from_UUID: java.util.UUID => EntityUUID = s => EntityUUID(s.toString)
//    implicit val _to_UUID: EntityUUID => java.util.UUID = s => java.util.UUID.fromString(s.id)

//    def validate_from_String(s: String): \/[InvalidUUIDinURLError, UUID] = {
//      val u: Regex =
//        """[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}""".r
//      val e: Either[InvalidUUIDinURLError, String] = u.findFirstIn(s).toRight(InvalidUUIDinURLError(s"$s is crappy for uuid"))
//
//      val r: \/[InvalidUUIDinURLError, UUID] = \/.fromEither(e).map(UUID._from_string(_))
//      r
//    }

  }

//  trait UUIDCompare[T] extends Data{
//    def isUUIDEq(x: T, y: T): Boolean
//  }

//  object UUIDCompare {
//
//    implicit class PimperImplicitConverterClass[T: UUIDCompare](toBePimped: T) {
//      def compare =
//        (x: T) => (y: T) => implicitly[UUIDCompare[T]].isUUIDEq(x, y)
//
//      def isUUIDEq = compare(toBePimped)(_)
//
//      def ~ = isUUIDEq
//    }
//
//  }
}