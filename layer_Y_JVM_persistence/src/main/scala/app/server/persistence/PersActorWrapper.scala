package app.server.persistence

import akka.actor.ActorRef
import akka.util.Timeout
import app.server.persistence.persActor.Commands.{CreateEntityPACommand, CreateEntityPAResponse, GetStatePACommand, GetStatePAResponse, SetStatePACommand, SetStatePAResponse, UpdateEntityPACommand, UpdateEntityPAResponse}

import app.shared.data.model.Entity.Data
import app.shared.data.ref.RefValDyn
import app.testHelpersShared.data.TestDataLabels.TestDataLabel
import app.testHelpersShared.implicits.ForTestingOnly

import scala.concurrent.Future


/**
  *
  * Ez mit csinál ?
  * Ki használja ?
  * Minek van erre szükség?
  *
  * // Random UUID: 429fc025b1454677bc18fd9eb8acf28f
  * // commit 261bba625a6dc3bfc178a1d578cd104b23cf6437
  * // Date: Tue Aug  7 09:08:46 EEST 2018
  *
  */
trait PersActorWrapperIF{
  def getState: Future[GetStatePAResponse]

  def updateEntity(rfvd: RefValDyn): Future[UpdateEntityPAResponse]

  def createEntity(e:Data):Future[CreateEntityPAResponse]

  def setState(s:TestDataLabel): Future[SetStatePAResponse]
}

class PersActorWrapper(private[this] val actor: ActorRef) extends PersActorWrapperIF{
  import akka.pattern.ask
  import akka.util.Timeout

  import scala.concurrent.duration._

  override def getState: Future[GetStatePAResponse] =
    ask(actor, GetStatePACommand)(Timeout.durationToTimeout(1 seconds))
      .mapTo[GetStatePAResponse]

  override def updateEntity(rfvd: RefValDyn): Future[UpdateEntityPAResponse] =
    ask(actor, UpdateEntityPACommand(rfvd))(Timeout.durationToTimeout(1 seconds))
      .mapTo[UpdateEntityPAResponse]

  override def createEntity(e:Data):Future[CreateEntityPAResponse]=
    ask(actor, CreateEntityPACommand(e))(Timeout.durationToTimeout(1 seconds)).mapTo[CreateEntityPAResponse]

  override def setState(s:TestDataLabel): Future[SetStatePAResponse] =
    ask(actor, SetStatePACommand(s))(Timeout.durationToTimeout(1 seconds))
    .mapTo[SetStatePAResponse]

}
