package shared.communication.persActorCommands.auth

import io.circe.{Decoder, Encoder, HCursor, Json}
import shared.dataStorage.model.PWDNotHashed
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._
import shared.dataStorage.relationalWrappers.Ref
import shared.communication.persActorCommands.PersActorQuery

//@JsonCodec
case class QueryAuthWrapper[PAQ <: PersActorQuery](
  query: PAQ,
  pwd:   PWDNotHashed)

// https://circe.github.io/circe/codecs/custom-codecs.html

object QueryAuthWrapper {

  implicit def encoder[PAQ <: PersActorQuery: Encoder](
  ): Encoder[QueryAuthWrapper[PAQ]] =
    new Encoder[QueryAuthWrapper[PAQ]] {

      final def apply(a: QueryAuthWrapper[PAQ]): Json = Json.obj(
        ("query", a.query.asJson),
        ("pwd_not_hashed", Json.fromString(a.pwd.str))
      )
    }

  implicit def decoder[
    PAQ <: PersActorQuery: Decoder
  ]: Decoder[QueryAuthWrapper[PAQ]] =
    new Decoder[QueryAuthWrapper[PAQ]] {

      final def apply(
        c: HCursor
      ): Decoder.Result[QueryAuthWrapper[PAQ]] =
        for {
          pwd_not_hashed <- c.downField("pwd_not_hashed").as[String]
          foo <- c.downField("query").as[PAQ]
        } yield {
          new QueryAuthWrapper(foo, PWDNotHashed(pwd_not_hashed))
        }
    }

}

// todo-now JSON encoding for subtypes

// option like cucc
