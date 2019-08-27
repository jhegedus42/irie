package app.server.httpServer.routes.logic.typeSafePersistenceHeaven.typelessHell.state
import app.server.httpServer.routes.logic.typeSafePersistenceHeaven.typelessHell.state.refs.UntypedRef

private [typelessHell] case class ApplicationStateMap(
    val map: Map[UntypedRef, ApplicationStateMapEntry] = Map.empty
) {


}
