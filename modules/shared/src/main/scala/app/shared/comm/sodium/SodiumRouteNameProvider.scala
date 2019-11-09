package app.shared.comm.sodium

import app.shared.entity.entityValue.EntityType

import scala.reflect.ClassTag

class ClassTagPrivoders[
  RT <: SodiumCRUDReq[V],
  V  <: EntityType[V]
]() {
  implicit def ct: ClassTag[RT] = implicitly[ClassTag[RT]]
  implicit def e:  ClassTag[V]  = implicitly[ClassTag[V]]
}

object SodiumRouteNameProvider {


  def getRouteName[V<:EntityType[V],Req<:SodiumCRUDReq[V]](
  )(
    implicit
    ct_pl:  ClassTag[V],
    ct_req: ClassTag[Req]
  ): SodiumRouteName[Req,V] = {

    val name_req = ct_req.runtimeClass.getSimpleName
    val name_pl  = ct_pl.runtimeClass.getSimpleName

    SodiumRouteName(s"route_${name_req}_${name_pl}_auto_generated")
  }

}