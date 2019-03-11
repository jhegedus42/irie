package app.shared.rest.routes.crudRequests

import app.shared.data.model.Entity.{Data, Entity}
import app.shared.data.model.{LineText, User}
import app.shared.data.ref.{Ref, RefVal}
import io.circe

import scala.reflect.ClassTag
import scalaz.\/

object GetEntityRequest {
//  case class GetEntityReqParam[E<:Entity](ref:Ref[E])
//  case class GetEntityReqResult[E<:Entity](res: \/[circe.Error,RefVal[E]])

  def pathForGetEntityRoute_serverSideCode[E <: Entity: ClassTag]: String =
    "getSingleEntity" +
      implicitly[ClassTag[E]].runtimeClass.getSimpleName

  def parameterReprInURL[E <: Entity](rv: Ref[E] ): String = {
    val u = rv.uuid.id
    s"?id=$u"
  }

  def queryURL[E <: Entity:ClassTag](rv: Ref[E] ): String =
    "/" +
      pathForGetEntityRoute_serverSideCode[E] +
      parameterReprInURL( rv )

}
