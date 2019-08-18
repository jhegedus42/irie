package app.shared.dataModel.value.asString

import app.shared.dataModel.value.EntityValue
import app.shared.dataModel.value.refs.Entity
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
