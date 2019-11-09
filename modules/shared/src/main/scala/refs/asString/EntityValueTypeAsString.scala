package refs.asString

//import io.circe.generic.JsonCodec
import refs.EntityType

import scala.reflect.ClassTag

//import io.circe.generic.auto._
//import io.circe.syntax._
import io.circe.generic.JsonCodec

//@Lenses
@JsonCodec
case class EntityValueTypeAsString(type_as_string: String)

object EntityValueTypeAsString {

  def getEntityValueTypeAsString[T <: EntityType[T]](
    implicit t: ClassTag[T]
  ): EntityValueTypeAsString =
    EntityValueTypeAsString(t.runtimeClass.getSimpleName)

}
