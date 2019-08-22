package app.server.httpServer.routes.persistenceProvider.persistentActor.state
import app.shared.entity.Entity
import app.shared.entity.asString.EntityAsString
import app.shared.entity.entityValue.EntityValue
import io.circe.Encoder
import app.shared.entity.asString.EntityAsString
import monocle.macros.Lenses
import app.shared.entity.asString.EntityAsString
import monocle.macros.Lenses

@Lenses
case class ApplicationStateMapEntry(
                                     untypedRef:    UntypedRef,
                                     entityAsString:EntityAsString
                                   ){
  def asSimpleString():String ={
    val s1=untypedRef.asSimpleString()
    val s2=entityAsString.entityValueAsToString.toStringResult
    s"$s1 ----- $s2"
  }
}
