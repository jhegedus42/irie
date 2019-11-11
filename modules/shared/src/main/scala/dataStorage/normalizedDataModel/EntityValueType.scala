package dataStorage.normalizedDataModel

import dataStorage.RelationalAndVersionedDataRepresentationFramework.{EntityValueWithVersionAndIdentity, EntityVersionAndEntityIdentity}

import io.circe.syntax._
import io.circe.generic.auto._
import io.circe.generic.JsonCodec

import io.circe.parser._

/**
  *
  * This is the type of the value that an entity holds.
  *
  * @tparam T
  */

//@JsonCodec
sealed trait EntityValueType[T <: EntityValueType[T]]

//@JsonCodec
object EntityValueType {
  implicit def makeFromValue[V <: EntityValueType[V]](
    v: V
  ): EntityValueWithVersionAndIdentity[V] =
    EntityValueWithVersionAndIdentity(v)
}

@JsonCodec
case class Note(
  title:   String,
  content: String,
  owner:   EntityVersionAndEntityIdentity[User])
    extends EntityValueType[Note]

@JsonCodec
case class Image(
  title:     String,
  content:   String,
  reference: Option[EntityVersionAndEntityIdentity[Note]])
    extends EntityValueType[Image]


@JsonCodec
case class User(
  name:           String,
  favoriteNumber: Int,
  password:       String = "titok")
    extends EntityValueType[User]


@JsonCodec
case class NoteFolder(
                       user: EntityVersionAndEntityIdentity[User],
                       name: String)
    extends EntityValueType[NoteFolder]
