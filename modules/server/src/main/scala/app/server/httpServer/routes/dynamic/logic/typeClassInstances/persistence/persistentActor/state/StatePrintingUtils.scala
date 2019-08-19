package app.server.httpServer.routes.dynamic.logic.typeClassInstances.persistence.persistentActor.state

object StatePrintingUtils {

  def printApplicationState( state: ApplicationStateMap ): Unit = {

    val stateBeforeAsString = state.map.foldLeft( "" )( {
      ( b: String, tuple: ( UntypedRef, ApplicationStateMapEntry ) ) =>
        val newString = b +
          s"""
             |
             | vvvvvvvvvvv
             | Key:
             | ${tuple._1}
             |
             | Entry:
             | ${tuple._2}
             |
             | ^^^^^^^^^^^
             |
            """.stripMargin
        newString
    } )

    val keysAsSimpleList: Seq[String] = state.map.toSeq.map(
      (f: ( UntypedRef, ApplicationStateMapEntry )) => f._2.asSimpleString()
    )

    println("\nvvvv-vvvv------STATE------vvvv-vvvv")
    println( s"Number of entries: ${state.map.size}\n" )
    println( s"Hashcode of map: ${state.map.hashCode()}\n" )
    keysAsSimpleList.foreach( println )
    println(  "^^^^-^^^^------STATE------^^^^-^^^^\n")

  }

  def printStateChange( stateChange: StateChange ): Unit = {

    println( "\n\n-------------- STATE BEFORE: -------------------\n\n" )

    printApplicationState( stateChange.before )

    println( "\n\n-------------- STATE AFTER: -------------------\n\n" )

    printApplicationState( stateChange.after )

  }

}
