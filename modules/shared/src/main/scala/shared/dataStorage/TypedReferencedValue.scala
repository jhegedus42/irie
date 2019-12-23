package shared.dataStorage

import io.circe.syntax._
import io.circe.generic.auto._
import io.circe.generic.JsonCodec
//import cats._
//import cats.derived
//import cats.implicits._
import shapeless.Typeable

@JsonCodec
case class TypedReferencedValue[E <: Value[E]](
  versionedEntityValue: VersionedValue[E],
  ref:                  Ref[E] = Ref[E]()) {

  def addTypeInfo(
  )(
    implicit
    typeable: Typeable[E]
  ): TypedReferencedValue[E] = {
    val r: Ref[E] = ref.addTypeInfo()(typeable)
    TypedReferencedValue(versionedEntityValue, r)
  }

  def addEntityOwnerInfo(r: Ref[User]): TypedReferencedValue[E] = {
    TypedReferencedValue(this.versionedEntityValue,
                         this.ref.addEntityOwnerInfo(r))
  }

  def bumpVersion: TypedReferencedValue[E] = {
    import monocle.macros.syntax.lens._
    this
      .lens(_.versionedEntityValue.version).set(
        this.versionedEntityValue.version.inc
      )

  }

}

object TypedReferencedValue {}
