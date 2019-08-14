package app.shared.comm

import app.shared.dataModel.value.EntityValue
import app.shared.dataModel.value.refs.TypedRefToEntity

import scala.reflect.ClassTag

object GetEntityURLs {
//  case class GetEntityReqParam[E<:Entity](ref:Ref[E])

  def pathForGetEntityRoute_serverSideCode[E <: EntityValue[E]: ClassTag]: String =
    "getSingleEntity" +
      implicitly[ClassTag[E]].runtimeClass.getSimpleName

  def parameterReprInURL[E <: EntityValue[E]](rv: TypedRefToEntity[E] ): String = {
    val u = rv.entityUUID.uuid
    s"?id=$u"
  }

  def queryURL[E <: EntityValue[E]:ClassTag](rv: TypedRefToEntity[E] ): String =
    "/" +
      pathForGetEntityRoute_serverSideCode[E] +
      parameterReprInURL( rv )

}
