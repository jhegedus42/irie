package app.server.httpServer.routes

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import app.server.httpServer.routes.dynamic.RequestRoute
import app.server.httpServer.routes.dynamic.logic.typeClassInstances.GetEntity
import app.server.httpServer.routes.dynamic.logic.typeClassInstances.persistence.PersistenceModule
import app.server.httpServer.routes.static.{IndexDotHtml, StaticRoutes}
import app.shared.comm.requests.{GetEntityPostRequest, SumIntPostRequest}
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.entityValue.values.{Note, NoteFolder, User}
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
      RequestRoute.getRoute[SumIntPostRequest]().route ~
      StaticRoutes.staticRootFactory( rootPageHtml )

    result
  }

  private def rootPageHtml: String = IndexDotHtml.getIndexDotHTML

  private def crudEntityRoute[V <: EntityValue[V]: ClassTag](
      implicit unTypedRefDecoder: Decoder[RefToEntity[V]],
      encoder:                    Encoder[Entity[V]],
      decoder:                    Decoder[Entity[V]]
  ): Route = {

    //  todo-next create entity route
    //  todo-next update entity route

    getGetEntityRoute[V]
  }

  private def getGetEntityRoute[V <: EntityValue[V]: ClassTag](
      implicit decoder: Decoder[RefToEntity[V]],
      encoder:          Encoder[Entity[V]],
      entityDecoder:    Decoder[Entity[V]]
  ): Route = {
    implicit val logic = GetEntity
      .GetEntityLogic[V]( persistenceModule, entityDecoder, executionContext )
    val rr = RequestRoute.getRoute[GetEntityPostRequest[V]]

    rr.route
  }

}
