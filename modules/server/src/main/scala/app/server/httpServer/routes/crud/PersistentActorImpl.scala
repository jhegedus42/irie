package app.server.httpServer.routes.crud

import akka.actor.ActorLogging
import akka.persistence.{PersistentActor, RecoveryCompleted}
import shared.crudRESTCallCommands.persActorCommands.crudCMDs.{GetAllEntityiesForUserPersActCmd, InsertEntityPersActCmd, UpdateEntitiesPersActorCmd, UpdateEntityPersActCmd}
import shared.crudRESTCallCommands.persActorCommands.generalCmd.GeneralPersActorCmd
import shared.crudRESTCallCommands.{RequestReturnedWithError, RequestState, RequestSuccessfullyProcessedInPersistentActor}
import shared.crudRESTCallCommands.persActorCommands.{Response, ShutDown}
import shared.dataStorage.relationalWrappers.{RefToEntityOwningUser, UnTypedReferencedValue}
import shared.dataStorage.stateHolder.{EntityStorage, UserMap}
import shared.testingData.TestDataStore

import scala.io.{BufferedSource, Source}

class PersistentActorImpl(id: String)
    extends PersistentActor
    with ActorLogging {

  var state: EntityStorage = TestDataStore.testData

  override def receiveCommand: Receive = {

    case GeneralPersActorCmd(cmd: String) => {

      cmd match {
        case GeneralPersActorCmd.CommandStrings.saveData => {
          println("we need to save the data")
          import scala.io.Source

          println("file's old content:")
          Source.fromFile("data.json").foreach { x => print(x) }
          import java.io.File
          import java.io.PrintWriter
          val writer = new PrintWriter(new File("data.json"))

          writer.write(EntityStorage.getJSON(state.untypedMap))
          writer.close()

          println("\nfile's new content:")
          Source.fromFile("data.json").foreach { x => print(x) }
          println("----------------------")

          sender ! Response(GeneralPersActorCmd(cmd), None)
        }
        case _ => {
          println("command cannot be interpreted")
        }
      }

    }

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

    case UpdateEntitiesPersActorCmd(list, rs) => {
      // todo-now
      val res: Option[EntityStorage] = list.foldLeft(
        Some(state).asInstanceOf[Option[EntityStorage]]
      )({ (s, c) =>
        EntityStorage.updateOpt(s,
                                c.currentUnTypedReferencedValue,
                                c.newUTPVal)
      })
      if (res.isDefined) {
        state = res.get
        val toReturn = UpdateEntitiesPersActorCmd(
          list,
          RequestSuccessfullyProcessedInPersistentActor()
        )
        println(
          s"batch update on server succeeded, we return:\n$toReturn"
        )
        sender ! toReturn

      } else {

        val toReturnToSender = UpdateEntitiesPersActorCmd(
          list,
          RequestReturnedWithError(s"error on server with + $list")
        )

        println(s"update on server failed:\n$toReturnToSender")
        sender ! toReturnToSender
      }

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


      val res: BufferedSource =Source.fromFile("data.json")
      val res2 =res.mkString

      log.info(
        "Recovery completed \n\nState is:\n"
      )

      println("Data read:")
      println(res2)

      val loadedState=EntityStorage.getStateFromJSON(res2)
      println("Data Parsed:")
      println(loadedState)
      state=EntityStorage(loadedState)
      res.close()

    }
  }

}
