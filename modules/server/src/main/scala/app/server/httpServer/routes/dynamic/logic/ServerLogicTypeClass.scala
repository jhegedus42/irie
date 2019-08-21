package app.server.httpServer.routes.dynamic.logic

import app.shared.comm.PostRequest

import scala.concurrent.Future


  trait ServerLogicTypeClass[V <: PostRequest] {
    def getResult(param: V#Par ): Future[Option[V#Res]]
  }




