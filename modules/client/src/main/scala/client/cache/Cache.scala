package client.cache

import client.cache.CacheMap.UpdateEntityInCacheCommand
import client.sodium.core.{CellLoop, Stream, StreamSink, Transaction}
import client.ui.helpers.login.UserLoginStatusHandler
import shared.crudRequests.persActorCommands.InsertEntityIntoDataStore
import shapeless.Typeable
import io.circe.Decoder.Result
import io.circe._
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import shared.dataStorage.{TypedReferencedValue, User, Value}
import shared.dataStorage.stateHolder.UserMap

import scala.util.Try

//object CacheProvider {}

import client.sodium.core.Cell

case class Cache[V <: Value[V]](
  injectedTransformerStream: Stream[
    CacheMap[V] => CacheMap[V]
  ],
  typeName: String
)(
  implicit
  typeable: Typeable[V],
  encoder:  Encoder[TypedReferencedValue[V]]) {

  lazy val insertEntityStream: StreamSink[TypedReferencedValue[V]] =
    new StreamSink[TypedReferencedValue[V]]()

  lazy val updateEntityStream
    : StreamSink[UpdateEntityInCacheCommand[V]] =
    new StreamSink[UpdateEntityInCacheCommand[V]]()

  val cellLoop = Transaction.apply[CellLoop[CacheMap[V]]](
    { _ =>
      val insertEntityTransformer
        : Stream[CacheMap[V] => CacheMap[V]] = {

        val insertHandler: TypedReferencedValue[V] => Unit = {
          (x: TypedReferencedValue[V]) =>
            {

              val in: InsertEntityIntoDataStore =
                InsertEntityIntoDataStore.fromReferencedValue(x)

              AJAXCalls.ajaxCall(in, {
                x: Try[InsertEntityIntoDataStore] =>
                  println(x)
              })

            }

        }

        val addTypeInfo
          : TypedReferencedValue[V] => TypedReferencedValue[V] = {
          (x: TypedReferencedValue[V]) =>
            x.addTypeInfo().addEntityOwnerInfo(
                UserLoginStatusHandler.getUserLoginStatusDev.userOption.get.ref
              )
        }

        val is2 = insertEntityStream.map(addTypeInfo)

        is2.listen(insertHandler)

        is2.map(CacheMap.insertReferencedValueTransformer[V](_))
      }

      val updateEntityTransformerStream
        : Stream[CacheMap[V] => CacheMap[V]] = {
        updateEntityStream.map(
          // todonow 1 SEND AJAX REQ TO UPDATE USER
          //  use "val insertEntityTransformer" as a template
          //  to send an update Entity value request to server
          CacheMap.updateReferencedValueTransformer[V](_)
        )
      }

      lazy val transformer: Stream[CacheMap[V] => CacheMap[V]] =
        injectedTransformerStream
          .orElse(insertEntityTransformer).orElse(
            updateEntityTransformerStream
          )

      lazy val afterUpdate: Stream[CacheMap[V]] =
        transformer.snapshot(
          counterValue, { (f: CacheMap[V] => CacheMap[V], c: CacheMap[V]) =>
            f(c)
          }
        )

      lazy val counterValue: CellLoop[CacheMap[V]] =
        new CellLoop[CacheMap[V]]()

      counterValue.loop(afterUpdate.hold(CacheMap[V]()))

      counterValue
    }
  )

}

object Cache {

  lazy val streamToSetInitialCacheState =
    new StreamSink[UserMap]()

  implicit val user: Cache[User] =
    CacheMaker[User](streamToSetInitialCacheState)
      .getCache()

  //  val note:  Cache[Note]  = CacheMaker(s).makeCache[Note]()
  //  val image: Cache[Image] = CacheMaker(s).makeCache[Image]()

}
