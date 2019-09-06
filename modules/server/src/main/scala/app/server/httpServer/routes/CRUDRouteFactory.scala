package app.server.httpServer.routes

import akka.http.scaladsl.server.Route
import app.server.httpServer.routes.post.PostRouteForAkkaHttpFactory.getPostRoute
import app.server.httpServer.routes.post.routeLogicImpl.{
  GetRL,
  InsertRL,
  UpdateRL
}
import app.shared.comm.postRequests.{
  GetEntityRoute,
  InsertNewEntityRoute,
  UpdateEntityRoute
}
import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.refs.RefToEntityWithVersion
import io.circe.{Decoder, Encoder}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.PersistentServiceProvider

import scala.concurrent.ExecutionContextExecutor
import scala.reflect.ClassTag

case class CRUDRouteFactory(
    persistenceModule: PersistentServiceProvider,
    executionContext:  ExecutionContextExecutor
) {
  implicit val ec = executionContext: ExecutionContextExecutor
  def route[
      V <: EntityValue[V]: ClassTag
  ](
      implicit
      unTypedRefDecoder: Decoder[RefToEntityWithVersion[V]],
      encoder:           Encoder[Entity[V]],
      valueEncoder:      Encoder[V],
      decoder:           Decoder[Entity[V]],
      dpl:               Decoder[V]
  ): Route = {

    //  todo-next-1 update entity route

    import io.circe.generic.auto._

    implicit val insertRouteLogic = InsertRL(
      persistenceModule,
      decoder,
      encoder,
      valueEncoder,
      implicitly[ClassTag[V]],
      executionContext
    )

    implicit val updateRouteLogic = UpdateRL(persistenceModule,
                                             decoder,
                                             encoder,
                                             valueEncoder,
                                             implicitly[ClassTag[V]],
                                             executionContext)

    implicit val getRouteLogic =
      GetRL[V](persistenceModule, decoder, executionContext)

    getPostRoute[UpdateEntityRoute[V]].route ~
      getPostRoute[InsertNewEntityRoute[V]].route ~
      getPostRoute[GetEntityRoute[V]].route
  }

}
