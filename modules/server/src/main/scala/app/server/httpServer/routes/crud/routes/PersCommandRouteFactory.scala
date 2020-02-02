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
import io.circe
import io.circe.{Decoder, Encoder, Json}
import shared.communication.persActorCommands.{Query, Response}
import shared.communication.CanProvideRouteName
import shared.dataStorage.model.Value
import shared.communication.persActorCommands.auth.QueryAuthWrapper

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.reflect.ClassTag

case class PersCommandRouteFactory[
//  V  <: Value[V],
  PC <: Query
](val actor: ActorRef,
  dec:       Decoder[QueryAuthWrapper[PC]],
  enc:       Encoder[PC],
  encR:      Encoder[Response[PC]]
)(
  implicit
  actorSystem:              ActorSystem,
  executionContextExecutor: ExecutionContextExecutor,
  rnp:                      CanProvideRouteName[PC]
//  j:                        JSONConvertable[PC],
//  dec2: Decoder[PC],
//  ct:  ClassTag[PC]
) {
  val rn = rnp.getRouteName

  def getRoute: Route = {
    post {
      path(rn) {
        entity(as[String]) { s: String =>
          {
            println(s"req:\n$s")
//            val j = Json.fromString(s)
//            val res = dec.decodeJson(j)

            val res1 =
              PersCommandRouteFactory.decodeQuery(s)(dec, enc, encR)

            val res2: Future[Response[PC]] = getResult(res1)

            import io.circe.syntax._

            implicit val r = encR

            val res3: Future[String] = {
              res2.map((x: Response[PC]) => x.asJson.spaces2)
            }

            complete(res3)

          }
        }
      }
    }
  }

  def getResult(msg: Query): Future[Response[PC]] = {
    println(s"query sent to PA : $msg")
    import akka.pattern.ask

    import scala.concurrent.duration._
    implicit val timeout = Timeout(5 seconds)

    ask(actor, msg).mapTo[Response[PC]]

  }

}

object PersCommandRouteFactory {

  def decodeQuery[PC <: Query](
    s:    String
  )(dec:  Decoder[QueryAuthWrapper[PC]],
    enc:  Encoder[PC],
    encR: Encoder[Response[PC]]
  ) = {
    import circe.parser._

    val res: Either[circe.Error, QueryAuthWrapper[PC]] =
      decode[QueryAuthWrapper[PC]](s)(dec)

    println(s"decoded req: $res")

    val res1: QueryAuthWrapper[PC] = res.toOption.get
    res1
  }

}
