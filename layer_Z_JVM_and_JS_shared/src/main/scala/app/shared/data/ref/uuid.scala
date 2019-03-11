package app.shared.data.ref

import app.shared.{InvalidUUIDinURLError, StateOpsError}
import app.shared.data.model.Entity.Data

import scala.util.matching.Regex
import scalaz.{\/, \/-}

/**
  * Created by joco on 28/04/2017.
  */
object uuid {

  case class UUID(id: String){
    def isCorrect(): Boolean = UUID.validate_from_String(id).isRight
  }

  object UUID {
    def random(): UUID = UUID(java.util.UUID.randomUUID().toString)
    implicit val _from_string: String => UUID = s => UUID(s)
    implicit val _to_string: UUID => String = (u: UUID) => u.id
    implicit val _from_UUID: java.util.UUID => UUID = s => UUID(s.toString)
    implicit val _to_UUID: UUID => java.util.UUID = s =>
      java.util.UUID.fromString(s.id)

    def validate_from_String(s: String): \/[InvalidUUIDinURLError, UUID] = {
      val u: Regex =
        """[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}""".r
      val e: Either[InvalidUUIDinURLError, String] = u.findFirstIn(s).toRight(InvalidUUIDinURLError(s"$s is crappy for uuid"))

      val r: \/[InvalidUUIDinURLError, UUID] = \/.fromEither(e).map(UUID._from_string(_))
      r
    }

  }

  trait UUIDCompare[T] extends Data{
    def isUUIDEq(x: T, y: T): Boolean
  }

  object UUIDCompare {

    implicit class PimperImplicitConverterClass[T: UUIDCompare](toBePimped: T) {
      def compare =
        (x: T) => (y: T) => implicitly[UUIDCompare[T]].isUUIDEq(x, y)

      def isUUIDEq = compare(toBePimped)(_)

      def ~ = isUUIDEq
    }

  }
}
