package app.server.httpServer.routes.post.requestHandlerLogic

import app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.SumIntHandler
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
  implicit val sumIntInstance=SumIntHandler
}



