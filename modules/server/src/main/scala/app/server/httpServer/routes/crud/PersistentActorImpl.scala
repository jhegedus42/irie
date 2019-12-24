package app.server.httpServer.routes.crud

import akka.actor.ActorLogging
import akka.persistence.{PersistentActor, RecoveryCompleted}
import shared.crudRESTCallCommands.{
  RequestState,
  RequestSuccessfullyReturned
}
import shared.crudRESTCallCommands.persActorCommands.{
  GetAllEntityiesForUserPersActCmd,
  InsertEntityPersActCmd,
  ShutDown
}
import shared.dataStorage.{
  RefToEntityOwningUser,
  UnTypedReferencedValue
}
import shared.dataStorage.stateHolder.{EntityStorage, UserMap}
import shared.testingData.TestDataStore

class PersistentActorImpl(id: String)
    extends PersistentActor
    with ActorLogging {

  var state: EntityStorage = TestDataStore.testData

  override def receiveCommand: Receive = {
    case ShutDown =>
      println("shutting down persistent actor")
      context.stop(self)

    case GetAllEntityiesForUserPersActCmd(
        userRef: RefToEntityOwningUser,
        resp
        ) => {
      println(s"user uuid is : ${userRef.uuid}")
      val umap: UserMap = state.getUserMap(userRef)
      sender ! GetAllEntityiesForUserPersActCmd(userRef, Some(umap))
    }

    // todonow 1.1.1 create update handler in persistent actor

    case InsertEntityPersActCmd(
        entityToInsert: UnTypedReferencedValue,
        res:            RequestState
        ) => {
      println(s"entityToInsert is : ${entityToInsert}")
      val u        = entityToInsert.unTypedRef
      val newState = state.insert(u, entityToInsert)
      state = newState
      sender ! InsertEntityPersActCmd(
        entityToInsert,
        RequestSuccessfullyReturned()
      )
    }

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
