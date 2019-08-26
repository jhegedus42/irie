package app.server.httpServer.routes.persistence.typeSafeWorld.styx.typelessUnderWorld.state

import app.server.httpServer.routes.persistence.typeSafeWorld.styx.typelessUnderWorld.state.refs.UntypedRef
import app.shared.entity.asString.EntityAsString
import monocle.macros.Lenses

@Lenses
private[styx] case class ApplicationStateMapEntry(
    untypedRef:     UntypedRef,
    entityAsString: EntityAsString
) {

  def asSimpleString(): String = {
    val s1 = untypedRef.asSimpleString()
    val s2 = entityAsString.entityValueAsToString.toStringResult
    s"$s1 ----- $s2"
  }
}
