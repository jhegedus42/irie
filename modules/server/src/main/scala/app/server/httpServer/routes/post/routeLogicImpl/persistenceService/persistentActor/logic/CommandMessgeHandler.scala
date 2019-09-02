package app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.logic

import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.data.Commands.{InsertCommand, Update}

case class CommandMessgeHandler() {

  def handleUpdate(message: Update ): Unit = {
//    val oldState = state.getState
//
//    val event: events.UpdateEntityEventToBeSavedToTheJournal = {
//      events.UpdateEntityEventToBeSavedToTheJournal( command )
//    }
//
//    persist( event ) { evt: events.UpdateEntityEventToBeSavedToTheJournal =>
//      applyEvent( evt )
//    }
//
//    val stateChange =
//      StateChange( oldState, state.getState )
//
//    println(
//      "\n\n" +
//        "ReceiveCommand was called\n" +
//        "and matched the case 'InsertNewEntityCommand',\n" +
//        "size of maps in StateChange:\n"
//    )
//    println( stateChange.getSizeOfMapsBeforeAndAfter )
//    println()
//
//    // todo-one-day - handle errors related to "OCC" - "and such"
//    //  so the following response is "not the best", putting it
//    //  mildly
//
//    val resp: Responses.UpdateEntity_Command_Response =
//      UpdateEntity_Command_Response(
//        (Right( stateChange ) )
//      )
//
//    sender() ! resp

  }

  def handleInsert(command: InsertCommand ): Unit = {
      // todo-right-now

//    val oldState = state.getState
//
//    val event: events.CreateEntityEventToBeSavedToTheJournal = {
//      events.CreateEntityEventToBeSavedToTheJournal( command )
//    }
//
//    persist( event ) { evt: events.CreateEntityEventToBeSavedToTheJournal =>
//      applyEvent( evt )
//    }
//
//    val stateChange =
//      StateChange( oldState, state.getState )
//
//    println(
//      "\n\n" +
//        "ReceiveCommand was called\n" +
//        "and matched the case 'InsertNewEntityCommand',\n" +
//        "size of maps in StateChange:\n"
//    )
//    println( stateChange.getSizeOfMapsBeforeAndAfter )
//    println()
//
//    sender() ! Responses.InsertNewEntity_Command_Response( stateChange )

  }

}
