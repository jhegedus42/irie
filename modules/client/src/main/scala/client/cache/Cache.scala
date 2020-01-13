package client.cache

import client.sodium.core.{CellLoop, Stream, StreamSink, Transaction}
import client.ui.helpers.login.UserLoginStatusHandler
import shared.crudRESTCallCommands.persActorCommands.InsertEntityPersActCmd
import shapeless.Typeable
import shared.dataStorage.{Image, Note, NoteFolder, TypedReferencedValue, User, Value}
import io.circe._
import shared.dataStorage.stateHolder.UserMap

import scala.util.Try

//object CacheProvider {}

import client.sodium.core.Cell

case class Cache[V <: Value[V]: Encoder](
  setInitialValueTransformerStream: Stream[
    CacheMap[V] => CacheMap[V]
  ],
  typeName: String
)(
  implicit
  typeable: Typeable[V],
  encoder:  Encoder[TypedReferencedValue[V]]) {

  lazy val insertEntityStream: StreamSink[TypedReferencedValue[V]] =
    new StreamSink[TypedReferencedValue[V]]()

  lazy val updateEntityCommandStream
    : StreamSink[UpdateEntityInCacheCmd[V]] =
    new StreamSink[UpdateEntityInCacheCmd[V]]()

  val cellLoop: CellLoop[CacheMap[V]] = Transaction.apply[CellLoop[CacheMap[V]]](
    { _ =>
      val insertEntityTransformerStream
        : Stream[CacheMap[V] => CacheMap[V]] = {

        val insertHandler: TypedReferencedValue[V] => Unit = {
          (x: TypedReferencedValue[V]) =>
            {

              val in: InsertEntityPersActCmd =
                InsertEntityPersActCmd
                  .fromReferencedValue(x)

              AJAXCalls.sendCommandToServerViaAJAXCall(in, {
                x: Try[InsertEntityPersActCmd] =>
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

        lazy val updateHandler: UpdateEntityInCacheCmd[V] => Unit = {
          x: UpdateEntityInCacheCmd[V] =>
            AJAXCalls.updateEntityOnServer(x)

        }

        updateEntityCommandStream.listen(updateHandler)

        updateEntityCommandStream.map(
          CacheMap.updateReferencedValueTransformer[V](_)
        )
      }

      lazy val combinedCacheMapTransformerStream
        : Stream[CacheMap[V] => CacheMap[V]] =
        setInitialValueTransformerStream
          .orElse(insertEntityTransformerStream).orElse(
            updateEntityTransformerStream
          )

      lazy val updatedCacheMapStream: Stream[CacheMap[V]] =
        combinedCacheMapTransformerStream.snapshot(
          cacheMapCell, { (f: CacheMap[V] => CacheMap[V], c: CacheMap[V]) =>
            f(c)
          }
        )

      lazy val cacheMapCell: CellLoop[CacheMap[V]] =
        new CellLoop[CacheMap[V]]()

      cacheMapCell.loop(updatedCacheMapStream.hold(CacheMap[V]()))

      cacheMapCell
    }
  )

}

object Cache {

  lazy val streamToSetInitialCacheState =
    new StreamSink[UserMap]()

  implicit val userCache: Cache[User] =
    CacheMaker[User](streamToSetInitialCacheState)
      .getCache()

  implicit val noteCache: Cache[Note] =
    CacheMaker[Note](streamToSetInitialCacheState)
      .getCache()

  implicit val imgCache: Cache[Image] =
    CacheMaker[Image](streamToSetInitialCacheState)
      .getCache()

  implicit val noteFolderCache: Cache[NoteFolder] =
    CacheMaker[NoteFolder](streamToSetInitialCacheState)
      .getCache()

}
