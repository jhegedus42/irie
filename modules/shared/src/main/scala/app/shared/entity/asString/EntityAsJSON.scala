package app.shared.entity.asString

import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue
import io.circe.Decoder.Result
import io.circe.{Decoder, Json}

case class EntityAsJSON( json: Json )

object EntityAsJSON {
  def getEntity[V <: EntityValue[V]]( entityAsJSON: EntityAsJSON )
                                    (implicit d:Decoder[Entity[V]]) :
    Option[Entity[V]] = {
    val json=entityAsJSON.json
    val res: Result[Entity[V]] =d.decodeJson(json)
    res.toOption
  }

}
