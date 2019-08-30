package app.server.httpServer.routes.post.logic.persistence.operations.crudOps.persistentActor.data.state

import app.shared.entity.asString.EntityAsString
import monocle.macros.Lenses

@Lenses
private[persistentActor] case class StateMapEntry(
    untypedRef:     UntypedRef,
    entityAsString: EntityAsString
) {

  def asSimpleString(): String = {
    val s1 = untypedRef.asSimpleString()
    val s2 =
      entityAsString.entityValueAsToString.toStringResult
    s"$s1 ----- $s2"
  }
}
