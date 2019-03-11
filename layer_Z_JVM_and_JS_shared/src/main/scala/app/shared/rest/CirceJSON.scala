package app.shared.rest

import app.shared.data.model.Entity.{Data, Entity}
import app.shared.data.model.TypeAsString
import app.shared.data.ref.{Ref, RefVal}
import io.circe.Decoder.Result

import scala.reflect.ClassTag

/**
  * Created by joco on 10/05/2017.
  */
object CirceJSON {
  import io.circe._
  import io.circe.generic.semiauto._
  import io.circe.generic.auto._ // do not uncomment this -- needed for deriveDecoder

  import io.circe.parser._
  import io.circe.syntax._

//  implicit def decodeRefValWithTypeCheck[E <: Entity](implicit classTag: ClassTag[E], decoder: Decoder[Ref[E]]): Decoder[RefVal[E]] =
//    new Decoder[RefVal[E]] {
//      import io.circe.generic.auto._ // do not uncomment this -- needed for deriveDecoder
//      val decoder: Decoder[RefVal[E]] = deriveDecoder
//      //    final def apply(c: HCursor): Decoder.Result[RefVal[E]] = decoder.apply(c)
//      //              Left(DecodingFailure("Not implemented yet", c.history))
//      final def apply(c: HCursor): Decoder.Result[RefVal[E]] = {
//        Left(DecodingFailure("Not implemented yet  " + classTag, c.history))
//        val r: Result[RefVal[E]] = decoder.apply(c)
//        val et: EntityType = EntityType.make[E]
//        println(s"expected type ${classTag}")
//        r match {
//          case Left(a)                        => Left(a)
//          case Right(b) if b.r.entityType == et => Right(b)
//          case _ =>
//            Left(
//                  DecodingFailure(s"type mismatch while json decoding, expected type:" +
//                                  s" $classTag actual type: $et)",
//                                  c.history))
//        }
//      }
//    }

//  def typedRefDecode[E <: Entity](s: String )(implicit classTag: ClassTag[E] ): Either[Error, Ref[E]] = {
//    implicit def decodeRefWithTypeCheck[E <: Entity](implicit classTag: ClassTag[E] ): Decoder[Ref[E]] =
//      new Decoder[Ref[E]] {
//        val decoder: Decoder[Ref[E]] = deriveDecoder
//        final def apply(c: HCursor ): Decoder.Result[Ref[E]] = {
//          println( "my decoder is running" )
//          val r = decoder.apply( c )
//          val et: EntityType = EntityType.make[E]
//          println( s"expected type ${classTag}" )
//          r match {
//            case Left(  a )                       => Left(  a )
//            case Right( b ) if b.entityType == et => Right( b )
//            case _ =>
//              Left(
//                DecodingFailure(
//                  s"type mismatch while json decoding, expected type:" +
//                    s" $classTag actual type: $et)",
//                  c.history
//                )
//              )
//          }
//        }
//      }
//    decode[Ref[E]]( s )
//  }
//b367c298bb35479a81cca6efc51e112f commit 2ec8ad4ba0e9a407bff4a58217d78f3b774cbfe3 Fri Nov  3 19:34:33 EET 2017

  def typedRefValDecode[E <: Entity](
      s: String
    )(
      implicit
      classTag: ClassTag[E],
      decoder:  Decoder[RefVal[E]]
    ): Either[Error, RefVal[E]] = {
    // maybe this method is not needed because :
    // this

    // 70fa52d29a254b89bda07c0c52c9f68a commit 2ec8ad4ba0e9a407bff4a58217d78f3b774cbfe3 Fri Nov  3 19:40:06 EET 2017
    // takes care of the type safetyp problem already, as an assertion


    implicit def decodeRefValWithTypeCheck() = new Decoder[RefVal[E]] {

      final def apply(c: HCursor ): Decoder.Result[RefVal[E]] = {
        println( "my decoder is running" )

        val r:  Result[RefVal[E]] = decoder.apply( c )
        val et: TypeAsString        = TypeAsString.make[E]

        println( s"expected type ${classTag}" )

        r match {
          case Left( a )                          => Left( a )
          case Right( b ) if b.r.dataType == et   => Right(b)
          // if the entity type is the same
          case _ =>
            Left(
              DecodingFailure(
                s"type mismatch while json decoding, expected type:" +
                  s" $et, but decoder result: $r)",
                c.history
              )
            )
        }
      }
    }
    decode[RefVal[E]]( s )( decodeRefValWithTypeCheck() )

  }

}
