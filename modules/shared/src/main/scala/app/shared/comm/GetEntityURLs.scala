package app.shared.comm

import app.shared.dataModel.value.EntityValue
import app.shared.dataModel.value.refs.RefToEntity

import scala.reflect.ClassTag

object GetEntityURLs {
//  case class GetEntityReqParam[E<:Entity](ref:Ref[E])

  def pathForGetEntityRoute_serverSideCode[E <: EntityValue[E]: ClassTag]: String =
    "getSingleEntity" +
      implicitly[ClassTag[E]].runtimeClass.getSimpleName

  def parameterReprInURL[E <: EntityValue[E]](rv: RefToEntity[E] ): String = {
    val u = rv.entityIdentity.uuid
    s"?id=$u"
  }

  def queryURL[E <: EntityValue[E]:ClassTag](rv: RefToEntity[E] ): String =
    "/" +
      pathForGetEntityRoute_serverSideCode[E] +
      parameterReprInURL( rv )

}
