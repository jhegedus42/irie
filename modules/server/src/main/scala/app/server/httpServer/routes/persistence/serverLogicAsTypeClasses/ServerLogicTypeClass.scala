package app.server.httpServer.routes.persistence.serverLogicAsTypeClasses

import app.shared.comm.PostRequest

import scala.concurrent.Future


  trait ServerLogicTypeClass[Req <: PostRequest] {
    def getResult(param: Req#Par ): Future[Option[Req#Res]]
  }

object ServerLogicTypeClass {
  implicit val sumIntInstance=SumIntInstance
}




