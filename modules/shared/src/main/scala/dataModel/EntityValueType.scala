package dataModel

import entity.{Entity, Ref}

import io.circe.syntax._
import io.circe.generic.auto._
import io.circe.generic.JsonCodec

import io.circe.parser._

//@JsonCodec
sealed trait EntityValueType[T <: EntityValueType[T]]

//@JsonCodec
object EntityValueType {
  implicit def makeFromValue[V <: EntityValueType[V]](
    v: V
  ): Entity[V] =
    Entity(v)
}

@JsonCodec
case class Note(
  title:   String,
  content: String,
  owner:   Ref[User])
    extends EntityValueType[Note]

@JsonCodec
case class Image(
  title:     String,
  content:   String,
  reference: Option[Ref[Note]])
    extends EntityValueType[Image]


@JsonCodec
case class User(
  name:           String,
  favoriteNumber: Int,
  password:       String = "titok")
    extends EntityValueType[User]


@JsonCodec
case class NoteFolder(
  user: Ref[User],
  name: String)
    extends EntityValueType[NoteFolder]
