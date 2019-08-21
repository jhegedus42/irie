package app.server.httpServer.routes.routeProviders.dynamicRouteProviders.serverLogicAsTypeClasses

import app.shared.comm.PostRequest

import scala.concurrent.Future


  trait ServerLogicTypeClass[V <: PostRequest] {
    def getResult(param: V#Par ): Future[Option[V#Res]]
  }

object ServerLogicTypeClass {
  implicit val sumIntInstance=SumIntInstance
}




