package app.server.httpServer.routes.post.routeLogicImpl.logic.read

import app.server.httpServer.routes.post.RouteLogic
import app.server.httpServer.routes.post.routeLogicImpl.persistentActor.PersistentActorWhisperer
import app.shared.comm.postRequests.LoginReq
import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.values.User
import app.shared.utils.UUID_Utils.EntityIdentity

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

    def f(
      os: Option[Set[EntityWithRef[User]]]
    )  = {
      os.flatMap(
        _.filter(
          p =>
            (p.entityValue.name == param.userName &&
              p.entityValue.password == param.password)
        ).headOption
      )
    }

    paw
      .getAllEntitiesWithLatestVersion[User].map(f).map(
        LoginReq.Res.apply
      )
  }

  override def getRouteName: String =
    "debug 319CCAEA2DE4 - get route logic"

}

object LoginLogic {}
