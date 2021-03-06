package shared.dataStorage.relationalWrappers

import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._
import shapeless.Typeable
import shared.dataStorage.model.{TypeName, User, Value}

@JsonCodec
case class Ref[V <: Value[V]](unTypedRef: UnTypedRef = UnTypedRef()) {

  def addTypeInfo(
  )(
    implicit
    typeable: Typeable[V]
  ): Ref[V] =
    Ref(unTypedRef.addTypeInfo[V](typeable))

  def addEntityOwnerInfo(r: Ref[User]): Ref[V] = {
    Ref(unTypedRef.addEntityOwnerInfo(r))
  }
}

object Ref {
  //
}

@JsonCodec
case class EntityVersion(versionNumber: Integer = 0) {
  def inc: EntityVersion = EntityVersion(versionNumber + 1)
}

@JsonCodec
case class UnTypedRef(
  typeName: Option[TypeName] = None,
  uuid:     String           = java.util.UUID.randomUUID().toString,
  refToEntityOwningUser: RefToEntityOwningUser =
    RefToEntityOwningUser()) {

  def addTypeInfo[V <: Value[V]](
    implicit
    typeable: Typeable[V]
  ): UnTypedRef = {
    val name: String =
      implicitly[Typeable[V]].describe.toString
    this.copy(typeName = Some(TypeName(name)))
  }

  def addEntityOwnerInfo(r: Ref[User]) = {
    val entityOwningUser =
      RefToEntityOwningUser.makeFromRef(r)
    this.copy(refToEntityOwningUser = entityOwningUser)
  }
}

object UnTypedRef {

  def toTypedRef[V <: Value[V]](unTypedRef: UnTypedRef): Ref[V] = {
    Ref[V](unTypedRef)
  }

  def make[V <: Value[V]](
    implicit
    typeable: Typeable[V]
  ): UnTypedRef = UnTypedRef().addTypeInfo[V](typeable)

//  def makeUTR[V <: Value[V]](
//    utr: UnTypedRef
//  )(
//    implicit
//    typeable: Typeable[V]
//  ): UnTypedRef = UnTypedRef().addTypeInfo[V](typeable)

  def getTypeNameAsString[V <: Value[V]](
    implicit
    typeable: Typeable[V]
  ): String =
    implicitly[Typeable[V]].describe.toString
}
