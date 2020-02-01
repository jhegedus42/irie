package app.server.httpServer.routes.crud.routes

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.server.Directives.{
  as,
  complete,
  entity,
  path,
  post
}
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import shared.communication.persActorCommands.{
  PersActorQuery,
  Response
}
import shared.communication.{
  CanProvideRouteName,
  JSONConvertable
}
import shared.dataStorage.model.Value

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.reflect.ClassTag

case class PersCommandRouteWithAuthFactory[
  //  V  <: Value[V],
  PC <: PersActorQuery
](val actor: ActorRef
)(
  implicit
  actorSystem:              ActorSystem,
  executionContextExecutor: ExecutionContextExecutor,
  rnp:                      CanProvideRouteName[PC],
  j:                        JSONConvertable[PC],
  jResp:                    JSONConvertable[Response[PC]],
  ct:                       ClassTag[PC]) {

  def getRoute: Route = {
    post {
      path(rnp.getRouteName) {
        entity(as[String]) { s: String =>
          {

            val fs = getResult(j.fromJSONToObject(s))
              .map(x => jResp.toJSON(x))

            complete(fs)

          }
        }
      }
    }
  }

  def getResult(msg: PersActorQuery): Future[Response[PC]] = {
    import akka.pattern.ask

    import scala.concurrent.duration._
    implicit val timeout = Timeout(5 seconds)

    ask(actor, msg).mapTo[Response[PC]]

  }

}