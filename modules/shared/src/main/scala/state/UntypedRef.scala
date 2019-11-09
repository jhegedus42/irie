package state

import refs.{EntityIdentityUntyped, EntityType, EntityVersion, RefToEntityWithVersion}
import refs.asString.EntityValueTypeAsString
import monocle.macros.Lenses

@Lenses
case class UntypedRef(
  entityValueTypeAsString: EntityValueTypeAsString,
  entityIdentity:          EntityIdentityUntyped = EntityIdentityUntyped(),
  entityVersion:           EntityVersion = EntityVersion()) {

  def asSimpleString(): String = {
    s"${this.entityIdentity.uuid} " +
      s"${this.entityValueTypeAsString.type_as_string} " +
      s"${entityVersion.versionNumberLong}"
  }

//  def stripVersion(): UntypedRefWithoutVersion =
//    UntypedRefWithoutVersion(entityValueTypeAsString, entityIdentity)
  def bumpVersion: UntypedRef = {
    import monocle.macros.syntax.lens._
    val res: UntypedRef = this
      .lens(
        _.entityVersion.versionNumberLong
      )
      .modify(
        x => x + 1
      )
    res
  }
}

object UntypedRef {

  implicit def makeFromRefToEntity[T <: EntityType[T]](
    refToEntity: RefToEntityWithVersion[T]
  ): UntypedRef = {
    UntypedRef(
      entityValueTypeAsString = refToEntity.entityValueTypeAsString,
      entityIdentity          = refToEntity.entityIdentity.stripType,
      entityVersion           = refToEntity.entityVersion
    )
  }

  def getTypedRef[T <: EntityType[T]](
    untypedRef: UntypedRef
  ): RefToEntityWithVersion[T] = {
    RefToEntityWithVersion(untypedRef.entityValueTypeAsString,
                           untypedRef.entityIdentity.toTyped,
                           untypedRef.entityVersion)
  }

}
