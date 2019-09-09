package app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.testRelatedOperations

import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.{EPOPExecutor, ElementaryPersistenceOperation}
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.ElementaryPersistenceOperation.{OperationResult, OperatonParameter}
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.occ.OCCVersion
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.testRelatedOperations.Reset.ResetStateEPOP.{ResetEPOPPar, ResetEPOPRes}
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.PersistentActorWhisperer

import scala.concurrent.Future

object Reset {

  trait ResetStateEPOP
      extends ElementaryPersistenceOperation[Nothing] {
    override type Par = ResetEPOPPar
    override type Res = ResetEPOPRes
  }

  object ResetStateEPOP {

    case class ResetEPOPRes(goodOrBad:Either[String,String])
        extends OperationResult

    case class ResetEPOPPar() extends  OperatonParameter

    implicit val inst = ResetEPOPExecuterInstance
  }

  object ResetEPOPExecuterInstance
    extends EPOPExecutor[Nothing, ResetStateEPOP] {
    override def execute(par: ResetStateEPOP.ResetEPOPPar)(
      implicit whisperer:   PersistentActorWhisperer
    ): Future[ResetStateEPOP.ResetEPOPRes] = {
      whisperer.resetTheState()
    }

    override def getOCCVersion: OCCVersion = ???
  }



}
