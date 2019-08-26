package app.server.httpServer.routes.persistence.notTypeSafeWorld

import app.server.httpServer.routes.persistence.notTypeSafeWorld.state.UntypedRef
import app.shared.entity.asString.EntityAsString
import monocle.macros.Lenses

@Lenses
private[persistence] case class ApplicationStateMapEntry(
    untypedRef:     UntypedRef,
    entityAsString: EntityAsString
) {

  def asSimpleString(): String = {
    val s1 = untypedRef.asSimpleString()
    val s2 = entityAsString.entityValueAsToString.toStringResult
    s"$s1 ----- $s2"
  }
}
