package app.server.httpServer.routes.post.routeLogicImpl.logic.read

import app.server.httpServer.routes.post.RouteLogic
import app.server.httpServer.routes.post.routeLogicImpl.persistentActor.PersistentActorWhisperer
import app.shared.comm.postRequests.LoginReq
import scala.concurrent.{ExecutionContextExecutor, Future}

case class LoginLogic(
)(
  implicit
  paw:             PersistentActorWhisperer,
  contextExecutor: ExecutionContextExecutor)
    extends RouteLogic[LoginReq] {

  override def getHttpReqResult(
    param: LoginReq.Par
  ): Future[LoginReq.Res] = {
//    paw.getAllUserRefs.map(LoginReq.Res(_))

    val password=param.password

    val userName=param.userName

    // let's assume that user name is unique

    ??? // todo-now 1
  }

  override def getRouteName: String =
    "debug 319CCAEA2DE4 - get route logic"

}

object LoginLogic {

//  implicit def getLogic : LoginLogic = ??? // todo-later ?? => what is this here ?

}
