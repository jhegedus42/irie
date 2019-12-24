package app.server.httpServer.routes.crud

import akka.actor.ActorLogging
import akka.persistence.{PersistentActor, RecoveryCompleted}
import shared.crudRESTCallCommands.{
  RequestReturnedWithError,
  RequestState,
  RequestSuccessfullyProcessedInPersistentActor
}
import shared.crudRESTCallCommands.persActorCommands.{
  GetAllEntityiesForUserPersActCmd,
  InsertEntityPersActCmd,
  ShutDown,
  UpdateEntityPersActCmd
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

    case UpdateEntityPersActCmd(unTypedReferencedValue,
                                newValue,
                                requestState) => {

      // todonow 1.1.1 create update handler in persistent actor

//      val u        = unTypedRef
      val newStateOpt = state.update(unTypedReferencedValue, newValue)

      if (newStateOpt.isDefined) {
        state = newStateOpt.get
        sender ! UpdateEntityPersActCmd(
          unTypedReferencedValue,
          newValue,
          RequestSuccessfullyProcessedInPersistentActor()
        )
      } else {
        sender ! UpdateEntityPersActCmd(
          unTypedReferencedValue,
          newValue,
          RequestReturnedWithError(
            s"OCC Error While Updating an Entity: \n" +
              "the command was:\n" +
              "$UpdateEntityPersActCmd"
          )
        )

      }

    }

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
        RequestSuccessfullyProcessedInPersistentActor()
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
