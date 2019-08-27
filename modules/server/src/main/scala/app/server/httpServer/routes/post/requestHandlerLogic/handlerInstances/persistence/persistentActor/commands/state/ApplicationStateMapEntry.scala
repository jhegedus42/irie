package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.persistentActor.commands.state

import app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.persistentActor.commands.state.refs.UntypedRef
import app.shared.entity.asString.EntityAsString
import monocle.macros.Lenses

@Lenses
private[persistentActor] case class ApplicationStateMapEntry(
  untypedRef:     UntypedRef,
  entityAsString: EntityAsString) {

  def asSimpleString(): String = {
    val s1 = untypedRef.asSimpleString()
    val s2 =
      entityAsString.entityValueAsToString.toStringResult
    s"$s1 ----- $s2"
  }
}
