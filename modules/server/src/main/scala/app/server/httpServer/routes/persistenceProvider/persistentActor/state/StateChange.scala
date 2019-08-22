package app.server.httpServer.routes.persistenceProvider.persistentActor.state

case class StateChange(
    before: ApplicationStateMap,
    after:  ApplicationStateMap
) {

  def getSizeOfMapsBeforeAndAfter: String = {
    val res =
      s"Size of maps (in StateChange):\nbefore: ${before.map.size}\nafter: ${after.map.size}"
    res
  }

}
