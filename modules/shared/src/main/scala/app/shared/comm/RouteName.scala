package app.shared.comm

import scala.reflect.ClassTag

case class RouteName(name: String ) {
  def getPathNameForClient: String = "/" + name
}

object RouteName {

  def getRouteName[Req <: Request: ClassTag]
    : RouteName = {
    val name=implicitly[ClassTag[Req]].runtimeClass.getSimpleName
    RouteName( "route_" + name)
  }
}
