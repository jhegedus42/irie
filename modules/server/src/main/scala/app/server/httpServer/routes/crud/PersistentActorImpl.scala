package app.server.httpServer.routes.crud

import akka.actor.ActorLogging
import akka.persistence.{PersistentActor, RecoveryCompleted}
import shared.crudRESTCallCommands.{RequestReturnedWithError, RequestState, RequestSuccessfullyProcessedInPersistentActor}
import shared.crudRESTCallCommands.persActorCommands.{GetAllEntityiesForUserPersActCmd, InsertEntityPersActCmd, ShutDown, UpdateEntitiesPersActorCmd, UpdateEntityPersActCmd}
import shared.dataStorage.{RefToEntityOwningUser, UnTypedReferencedValue}
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

    case UpdateEntitiesPersActorCmd(list) =>{
        // todo-now
    }

    case UpdateEntityPersActCmd(unTypedReferencedValue,
                                newValue,
                                requestState) => {

      val newStateOpt = state.update(unTypedReferencedValue, newValue)

      if (newStateOpt.isDefined) {
        state = newStateOpt.get
        val toReturn = UpdateEntityPersActCmd(
          unTypedReferencedValue,
          newValue,
          RequestSuccessfullyProcessedInPersistentActor()
        )
        println(s"update on server succeeded, we return:\n$toReturn")
        sender ! toReturn
      } else {

        val toReturnToSender = UpdateEntityPersActCmd(
          unTypedReferencedValue,
          newValue,
          RequestReturnedWithError(
            s"OCC Error While Updating an Entity: \n" +
              "the command was:\n" +
              "$UpdateEntityPersActCmd"
          )
        )

        println(s"update on server failed:\n$toReturnToSender")
        sender ! toReturnToSender
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
