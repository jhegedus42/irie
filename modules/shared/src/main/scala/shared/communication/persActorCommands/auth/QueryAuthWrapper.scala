package shared.communication.persActorCommands.auth

import io.circe
import io.circe.Decoder.Result
import io.circe.{Decoder, Encoder, HCursor, Json, ParsingFailure}
import shared.communication.persActorCommands.auth.QueryAuthWrapper.decoder
import shared.dataStorage.model.PWDNotHashed
import io.circe.parser._
import io.circe.syntax._
import shared.communication.{CanProvideRouteName}
import shared.dataStorage.relationalWrappers.Ref
import shared.communication.persActorCommands.Query
import shared.communication.persActorCommands.crudCMDs.GetAllEntityiesForUserPersActCmd
import shared.communication.persActorCommands.generalCmd.AdminQuery

case class QueryAuthWrapper[PAQ <: Query](
  query: PAQ,
  pwd:   PWDNotHashed)
    extends Query

// https://circe.github.io/circe/codecs/custom-codecs.html

object QueryAuthWrapper {

  implicit def authQuery[
    PAQ <: Query: CanProvideRouteName
  ]: CanProvideRouteName[QueryAuthWrapper[PAQ]] =
    new CanProvideRouteName[QueryAuthWrapper[PAQ]] {
      lazy val rn = implicitly[CanProvideRouteName[PAQ]].getRouteName

      override def getRouteName: String =
        s"query_with_auth_for_$rn"
    }

  implicit def encoder[PAQ <: Query: Encoder](
  ): Encoder[QueryAuthWrapper[PAQ]] =
    new Encoder[QueryAuthWrapper[PAQ]] {

      final def apply(a: QueryAuthWrapper[PAQ]): Json = Json.obj(
        ("queryAsJSON", a.query.asJson),
        ("pwd_not_hashed", Json.fromString(a.pwd.str))
      )
    }

  def decoder[PAQ <: Query](
    dec: Decoder[PAQ]
  ): Decoder[QueryAuthWrapper[PAQ]] =
    new Decoder[QueryAuthWrapper[PAQ]] {

      final def apply(
        c: HCursor
      ): Decoder.Result[QueryAuthWrapper[PAQ]] = {

        val j = c.get[Json]("queryAsJSON")
        val s: Result[String] = c.get[String]("pwd_not_hashed")

        println(s"json: $j")
        println(s"string: $s")

        val j2: Json = j.toOption.get

        val res2: Result[PAQ] = dec.decodeJson(j2)

        println(res2)

        println("bla")
        for {
          res <- res2
          pnh <- s
        } yield (new QueryAuthWrapper(res, PWDNotHashed(pnh)))
      }
    }

}

// todo-now JSON encoding for subtypes

// option like cucc
