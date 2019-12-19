package client.cache

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
  injectedTransformer: Stream[
    CacheMap[V] => CacheMap[V]
  ],
  typeName: String
)(
  implicit
  typeable: Typeable[V],
  encoder:  Encoder[TypedReferencedValue[V]]) {

  lazy val insertEntityStream: StreamSink[TypedReferencedValue[V]] =
    new StreamSink[TypedReferencedValue[V]]()

  //todonow 1.1.2.1 send update command into this

  lazy val updateEntityStream: StreamSink[TypedReferencedValue[V]] =
    new StreamSink[TypedReferencedValue[V]]()

  val cellLoop = Transaction.apply[CellLoop[CacheMap[V]]](
    { _ =>
      val insertEntityTransformer
        : Stream[CacheMap[V] => CacheMap[V]] = {

        val updateAjax = {
          (x: TypedReferencedValue[V]) =>
            {

              println(
                s"here we should send an AJAX request to insert this new" +
                  s"value into the servers data store: $x"
              )

              val in: InsertEntityIntoDataStore =
                InsertEntityIntoDataStore.fromReferencedValue(x)

              AJAXCalls.ajaxCall(in, {
                x: Try[InsertEntityIntoDataStore] =>
                  println(x)
              })

            }

        }

        val addTypeInfo = { (x: TypedReferencedValue[V]) =>
          x.addTypeInfo().addEntityOwnerInfo(
              UserLoginStatusHandler.getUserLoginStatusDev.userOption.get.ref
            )
        }

        val is2 = insertEntityStream.map(addTypeInfo)

        is2.listen(updateAjax)

        is2.map(CacheMap.insertReferencedValue[V](_))
      }

      lazy val transformer: Stream[CacheMap[V] => CacheMap[V]] =
        injectedTransformer
          .orElse(insertEntityTransformer)

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
