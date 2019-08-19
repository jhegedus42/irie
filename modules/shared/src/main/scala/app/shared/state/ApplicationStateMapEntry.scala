package app.shared.state

import app.shared.entity.asString.EntityAsString



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

