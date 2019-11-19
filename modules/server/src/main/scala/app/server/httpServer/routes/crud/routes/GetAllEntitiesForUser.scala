package app.server.httpServer.routes.crud.routes

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.server.Directives.{as, complete, entity, path, post}
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import comm.crudRequests.{
  Command,
  GetAllEntityiesForUser,
  JSONConvertable,
  RouteName
}

import scala.concurrent.{ExecutionContextExecutor, Future}

case class GetAllEntitiesForUser(
  val actor: ActorRef
)(
  implicit
  actorSystem:              ActorSystem,
  executionContextExecutor: ExecutionContextExecutor) {
  val rnProvider = implicitly[RouteName[GetAllEntityiesForUser]]
  val rn         = rnProvider.getRouteName

  def getAllEntitiesRoute: Route = {
    post {
      path(rn) {
        entity(as[String]) { s =>
          {
            val i: JSONConvertable[GetAllEntityiesForUser] =
              implicitly[JSONConvertable[GetAllEntityiesForUser]]
            val getAllEntityiesForUser: GetAllEntityiesForUser =
              i.getObject(s)
            val f  = getAllEntitiesFuture(getAllEntityiesForUser)
            val fs = f.map(x => i.getJSON(x))
            complete(fs)
          }
        }
      }
    }
  }

  def getAllEntitiesFuture(msg: Command): Future[GetAllEntityiesForUser] = {
    import akka.pattern.ask

    import scala.concurrent.duration._
    implicit val timeout = Timeout(5 seconds)

    ask(actor, msg).mapTo[GetAllEntityiesForUser]

  }
}
