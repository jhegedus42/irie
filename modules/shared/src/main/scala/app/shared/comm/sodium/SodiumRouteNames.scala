package app.shared.comm.sodium

import app.shared.entity.entityValue.EntityType
trait SodiumCRUDReq[V <: EntityType[V]]

import scala.reflect.ClassTag

case class SodiumRouteName(name: String)

object RouteName {

  def getRouteName[Req <: SodiumCRUDReq[V], V <: EntityType[V]](
  )(
    implicit
    ct_pl:  ClassTag[V],
    ct_req: ClassTag[Req]
  ): SodiumRouteName = {

    val name_req = ct_req.runtimeClass.getSimpleName
    val name_pl  = ct_pl.runtimeClass.getSimpleName

    SodiumRouteName(s"route_${name_req}_${name_pl}_auto_generated")
  }

}
