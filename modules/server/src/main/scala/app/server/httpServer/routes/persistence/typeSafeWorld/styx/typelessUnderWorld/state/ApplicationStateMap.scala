package app.server.httpServer.routes.persistence.typeSafeWorld.styx.typelessUnderWorld.state
import app.server.httpServer.routes.persistence.typeSafeWorld.styx.typelessUnderWorld.state.refs.UntypedRef

private [typelessUnderWorld] case class ApplicationStateMap(
    val map: Map[UntypedRef, ApplicationStateMapEntry] = Map.empty
) {


}
