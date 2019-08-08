package app.shared.entity

import app.shared.entity.Entity.Entity

import scala.reflect.ClassTag

object GetEntityURLs {
//  case class GetEntityReqParam[E<:Entity](ref:Ref[E])

  def pathForGetEntityRoute_serverSideCode[E <: Entity[E]: ClassTag]: String =
    "getSingleEntity" +
      implicitly[ClassTag[E]].runtimeClass.getSimpleName

  def parameterReprInURL[E <: Entity[E]](rv: TypedRef[E] ): String = {
    val u = rv.uuid.id
    s"?id=$u"
  }

  def queryURL[E <: Entity[E]:ClassTag](rv: TypedRef[E] ): String =
    "/" +
      pathForGetEntityRoute_serverSideCode[E] +
      parameterReprInURL( rv )

}
