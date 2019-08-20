package app.shared.comm

import scala.reflect.ClassTag

case class RouteName(name: String)

object RouteName {

  def getRouteName[Req <: PostRequest]()
  (
    implicit ct_pl: ClassTag[Req#PayLoad],
    ct_req: ClassTag[Req]
  ) : RouteName = {

    val name_req = ct_req.runtimeClass.getSimpleName
    val name_pl = ct_pl.runtimeClass.getSimpleName

    RouteName(s"route_${name_req}_${name_pl}_auto_generated")
  }

}
