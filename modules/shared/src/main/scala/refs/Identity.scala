package refs
import dataModel.EntityType
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._

/**
  * Created by joco on 28/04/2017.
  */

@JsonCodec
case class Identity[V <: EntityType[V]](
  uuid: String = java.util.UUID.randomUUID().toString)
