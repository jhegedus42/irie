package refs

import monocle.macros.Lenses

@Lenses
case class EntityIdentityUntyped(
  uuid: String = java.util.UUID.randomUUID().toString) {
  def toTyped[V <: EntityType[V]] = EntityIdentity[V](uuid)
}
