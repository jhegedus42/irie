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

case class InsertEntityRouteFactory[V <: Value[V]](
  val actor: ActorRef
)(
  implicit
  actorSystem:              ActorSystem,
  executionContextExecutor: ExecutionContextExecutor) {

  val rnProvider =
    implicitly[CanProvideRouteName[
      InsertEntityIntoDataStore
    ]]

  val rn = rnProvider.getRouteName

  def getRoute: Route = {
    post {
      path(rn) {
        entity(as[String]) { s =>
          {

            val i
              : JSONConvertable[InsertEntityIntoDataStore] =
              implicitly[JSONConvertable[
                InsertEntityIntoDataStore
              ]]

            // todonow
            //  1.1.1.1 create insert entity route

            val getAllEntityiesForUser
              : InsertEntityIntoDataStore =
              i.getObject(s)

            val f = getResult(getAllEntityiesForUser)

            val fs = f.map(x => i.getJSON(x))

            complete(fs)

          }
        }
      }
    }
  }

  def getResult(
    msg: PersActorCommand
  ): Future[InsertEntityIntoDataStore] = {
    import akka.pattern.ask

    import scala.concurrent.duration._
    implicit val timeout = Timeout(5 seconds)

    ask(actor, msg).mapTo[InsertEntityIntoDataStore]

  }

}
