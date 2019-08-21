package app.server.httpServer.routes

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import app.server.httpServer.routes.dynamic.PostRoute
import app.server.httpServer.routes.dynamic.logic.typeClassInstances.GetEntityLogic
import app.server.httpServer.routes.dynamic.logic.typeClassInstances.persistence.PersistenceModule
import app.server.httpServer.routes.static.{IndexDotHtml, StaticRoutes}
import app.shared.comm.postRequests.{GetEntityReq, SumIntPostRequest}
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.entityValue.values.User
import app.shared.entity.{Entity, RefToEntity}

import scala.concurrent.ExecutionContextExecutor
import scala.reflect.ClassTag

private[httpServer] case class RoutesProvider( actorSystem: ActorSystem ) {

  private implicit lazy val executionContext: ExecutionContextExecutor =
    actorSystem.dispatcher

  val persistenceModule = PersistenceModule( executionContext )

  val route: Route = allRoutes

  import io.circe._
  import io.circe.generic.auto._

  private def allRoutes: Route = {

    val result: Route =
      crudEntityRoute[User] ~
      PostRoute.createPostRoute[SumIntPostRequest]().route ~
      StaticRoutes.staticRootFactory( rootPageHtml )

    result
  }

  private def rootPageHtml: String = IndexDotHtml.getIndexDotHTML

  private def crudEntityRoute[V <: EntityValue[V]: ClassTag](
      implicit unTypedRefDecoder: Decoder[RefToEntity[V]],
      encoder:                    Encoder[Entity[V]],
      decoder:                    Decoder[Entity[V]]
  ): Route = {

    //  todo-next-1 update entity route
    //  todo-next-3 create entity route => todo-now

    implicit val logic = GetEntityLogic[V]( persistenceModule, decoder, executionContext )

    PostRoute.createPostRoute[GetEntityReq[V]].route
  }


}
