package app.server.httpServer.persistence.persistentActor

import akka.actor.ActorRef
import app.server.httpServer.persistence.persistentActor.Commands.{GetAllState, GetStateResult}

import scala.concurrent.Future

private[persistence]
case class  PersActorWrapper(val actor: ActorRef ) {
  import akka.pattern.ask
  import akka.util.Timeout

  import scala.concurrent.duration._

  def getState: Future[GetStateResult] =
    ask( actor, GetAllState )( Timeout.durationToTimeout( 1 seconds ) ) .mapTo[GetStateResult]

//  def updateEntity(rfvd: RefValDyn): Future[UpdateEntityPAResponse] =
//    ask(actor, UpdateEntityPACommand(rfvd))(Timeout.durationToTimeout(1 seconds))
//      .mapTo[UpdateEntityPAResponse]

  def insertEntity(e:vx):Future[CreateEntityPAResponse]=
    ask(actor, CreateEntityPACommand(e))(Timeout.durationToTimeout(1 seconds)).mapTo[CreateEntityPAResponse]


//  def setState( s: TestDataLabel ): Unit =
//    ask( actor, SetState( s ) )( Timeout.durationToTimeout( 1 seconds ) )

}
