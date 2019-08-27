package app.server.httpServer.routes.post.requestHandler.handlers.persistence.actor.state
import app.server.httpServer.routes.post.requestHandler.handlers.persistence.actor.state.refs.UntypedRef

private [typelessHell] case class ApplicationStateMap(
    val map: Map[UntypedRef, ApplicationStateMapEntry] = Map.empty
) {


}
