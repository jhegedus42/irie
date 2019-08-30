package app.server.httpServer.routes.post

import app.server.httpServer.routes.post.logic.SumIntLogic
import app.shared.comm.PostRequest

import scala.concurrent.Future

/**
  *
  * TC at the end symbolizes : TypeClass
  *
  * @tparam Req
  */

trait Logic[Req <: PostRequest] {
    def getResult(param: Req#Par ): Future[Option[Req#Res]]
}

object Logic {
  implicit val sumIntInstance=SumIntLogic
}




