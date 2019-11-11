package comm.crudRequests

import dataStorage.RelationalAndVersionedDataRepresentationFramework.EntityValueWithVersionAndIdentity
import dataStorage.normalizedDataModel.{EntityValueType, User}
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._
import sun.java2d.pipe.SpanShapeRenderer.Simple

trait Req[V <: EntityValueType[V]] {}

@JsonCodec
case class Write[V <: EntityValueType[V]](
  par: EntityValueWithVersionAndIdentity[V],
  r:   Option[EntityValueWithVersionAndIdentity[V]])
    extends Req[V]

@JsonCodec
case class GetAllLatestEntitiesForOneUser[V <: EntityValueType[V]](
  p:   EntityValueWithVersionAndIdentity[User],
  res: Option[List[EntityValueWithVersionAndIdentity[V]]])
    extends Req[V]
