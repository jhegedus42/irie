package syncedNormalizedState.comm



import dataModel.EntityValueType

import scala.reflect.ClassTag

class ClassTagPrivoders[
  RT <: SodiumCRUDReq[V],
  V  <: EntityValueType[V]
](
  implicit val ct: ClassTag[RT],
  implicit val e:  ClassTag[V]
){

}

object SodiumRouteNameProvider {


  def getRouteName[V<:EntityValueType[V],Req<:SodiumCRUDReq[V]](
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