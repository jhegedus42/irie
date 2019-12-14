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
import comm.crudRequests.persActorCommands.{
  GetAllEntityiesForUser,
  InsertEntityIntoDataStore,
  PersActorCommand
}
import comm.crudRequests.{
  CanProvideRouteName,
  JSONConvertable
}
import dataStorage.Value

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.reflect.ClassTag

case class PersCommandRouteFactory[
//  V  <: Value[V],
  PC <: PersActorCommand
](val actor: ActorRef
)(
  implicit
  actorSystem:              ActorSystem,
  executionContextExecutor: ExecutionContextExecutor,
  rnp:                      CanProvideRouteName[PC],
  j:                        JSONConvertable[PC],
  ct:                       ClassTag[PC]) {

  def getRoute: Route = {
    post {
      path(rnp.getRouteName) {
        entity(as[String]) { s: String =>
          {

            // todonow
            //  1.1.1.1 create insert entity route

            val fs = getResult(j.fromJSONToObject(s))
              .map(x => j.toJSON(x))

            complete(fs)

          }
        }
      }
    }
  }

  def getResult(msg: PersActorCommand): Future[PC] = {
    import akka.pattern.ask

    import scala.concurrent.duration._
    implicit val timeout = Timeout(5 seconds)

    ask(actor, msg).mapTo[PC]

  }

}
