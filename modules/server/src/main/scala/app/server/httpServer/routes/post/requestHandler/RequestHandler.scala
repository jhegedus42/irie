package app.server.httpServer.routes.post.requestHandler

import app.server.httpServer.routes.post.requestHandler.handlers.SumIntHandler
import app.shared.comm.PostRequest

import scala.concurrent.Future


trait RequestHandler[Req <: PostRequest] {
    def getResult(param: Req#Par ): Future[Option[Req#Res]]
}

object RequestHandler {
  implicit val sumIntInstance=SumIntHandler
}




