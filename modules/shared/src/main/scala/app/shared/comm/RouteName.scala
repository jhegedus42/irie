package app.shared.comm

import scala.reflect.ClassTag

case class RouteName(name: String)

object RouteName {

  def getRouteName[RT<:PostRequestType,Req <: PostRequest[RT]](
  )(
    implicit
    ct_pl: ClassTag[Req#PayLoadT],
    //    ct_pl: ClassTag[V],
    ct_req: ClassTag[Req]
  ): RouteName = {

    val name_req = ct_req.runtimeClass.getSimpleName
    val name_pl  = ct_pl.runtimeClass.getSimpleName
//    val name_pl = "dummy"

    RouteName(s"route_${name_req}_${name_pl}_auto_generated")
  }

}
