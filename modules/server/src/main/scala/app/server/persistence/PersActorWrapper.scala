package app.server.persistence

import akka.actor.ActorRef
import app.shared.data.model.Entity.Data
import app.testHelpersShared.data.TestDataLabel

import scala.concurrent.Future


case class PersActorWrapper(private[this] val actor: ActorRef) {
  import akka.pattern.ask
  import akka.util.Timeout

  import scala.concurrent.duration._

  def getState: Future[GetStatePAResponse] =
    ask(actor, GetStatePACommand)(Timeout.durationToTimeout(1 seconds))
      .mapTo[GetStatePAResponse]

  def updateEntity(rfvd: RefValDyn): Future[UpdateEntityPAResponse] =
    ask(actor, UpdateEntityPACommand(rfvd))(Timeout.durationToTimeout(1 seconds))
      .mapTo[UpdateEntityPAResponse]

  def createEntity(e:Data):Future[CreateEntityPAResponse]=
    ask(actor, CreateEntityPACommand(e))(Timeout.durationToTimeout(1 seconds)).mapTo[CreateEntityPAResponse]

  def setState(s:TestDataLabel): Future[SetStatePAResponse] =
    ask(actor, SetStatePACommand(s))(Timeout.durationToTimeout(1 seconds))
    .mapTo[SetStatePAResponse]

}
