package app.server.httpServer.routes.crud

import akka.actor.ActorLogging
import akka.persistence.{PersistentActor, RecoveryCompleted}
import comm.crudRequests.{GetAllEntityiesForUser, ShutDown}
import dataStorage.RefToEntityOwningUser
import dataStorage.stateHolder.UserMap
import testingData.TestDataStore

class PersistentActorImpl(id: String)
    extends PersistentActor
    with ActorLogging {

  var state = TestDataStore.testData

  override def receiveCommand: Receive = {
    case ShutDown =>
      println("shutting down persistent actor")
      context.stop(self)

    case GetAllEntityiesForUser(userRef: RefToEntityOwningUser, resp) => {
      println(s"user uuid is : ${userRef.uuid}")
      val umap: UserMap = state.getUserMap(userRef)
      sender ! GetAllEntityiesForUser(userRef, Some(umap))
    }

    // todo now => insert entity for User

  }

  override def persistenceId: String = id

  override def receiveRecover: Receive = {
    case RecoveryCompleted => {
      log.info(
        "Recovery completed \n\nState is:\n"
      )
    }
  }
}
