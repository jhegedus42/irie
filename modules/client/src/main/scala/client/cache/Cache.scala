package client.cache

import client.cache.commands.{
  UpdateEntitiesInCacheCommand,
  UpdateEntityInCacheCmd
}
import client.cache.relationalOperations.CellOptionMonad.CellOption
import client.sodium.core.{CellLoop, Stream, StreamSink, Transaction}
import client.ui.helpers.login.UserLoginStatusHandler
import shared.crudRESTCallCommands.persActorCommands.InsertEntityPersActCmd
import shapeless.Typeable
import io.circe._
import shared.dataStorage.model.{
  Folder,
  HintForNote,
  Note,
  User,
  Value
}
import shared.dataStorage.relationalWrappers.{
  Ref,
  TypedReferencedValue
}
import shared.dataStorage.stateHolder.UserMap

import scala.util.Try

//object CacheProvider {}

import client.sodium.core.Cell

case class Cache[V <: Value[V]: Encoder](
  setCacheValue: Stream[
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

//  lazy val updateEntityStream
  lazy val updateEntitiesCommandStream
    : StreamSink[UpdateEntitiesInCacheCommand[V]] =
    new StreamSink[UpdateEntitiesInCacheCommand[V]]()

  val cellLoop: CellLoop[CacheMap[V]] = Transaction.apply[CellLoop[
    CacheMap[V]
  ]](
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

      val updateEntitiesTransformerStream
        : Stream[CacheMap[V] => CacheMap[V]] = {

        lazy val updateHandler
          : UpdateEntitiesInCacheCommand[V] => Unit = {
          x: UpdateEntitiesInCacheCommand[V] =>
            AJAXCalls.updateEntitiesOnServer(x)

        }

        updateEntitiesCommandStream.listen(updateHandler)

        updateEntitiesCommandStream.map(CacheMap.updateEntities[V](_))

      }

      lazy val combinedCacheMapTransformerStream
        : Stream[CacheMap[V] => CacheMap[V]] =
        setCacheValue
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

  def getAllEntites[V <: Value[V]](
  )(
    implicit
    c: Cache[V]
  ): CellOption[Set[TypedReferencedValue[V]]] = {
    CellOption.fromCell(c.cellLoop.map(_.cacheMap.values.toSet))
  }

  def resolveRef[V <: Value[V]](
    ref: Cell[Option[Ref[V]]]
  )(
    implicit
    c: Cache[V]
  ): Cell[Option[TypedReferencedValue[V]]] = {
    val cm: Cell[CacheMap[V]] = c.cellLoop

    val res: Cell[Option[TypedReferencedValue[V]]] = cm.lift(ref, {
      (x, ro: Option[Ref[V]]) =>
        for {
          r <- ro
          z <- x.resRef(r)
        } yield (z)
    })
    res
  }

  def resolveRefCO[V <: Value[V]](
    ref: CellOption[Ref[V]]
  )(
    implicit
    c: Cache[V]
  ): CellOption[TypedReferencedValue[V]] = {
    val res = resolveRef(ref.co)
    CellOption.fromCellOption(res)
  }

  def resolveListOfRefs[V <: Value[V]](
    lor: Cell[Option[List[Ref[V]]]]
  )(
    implicit
    c: Cache[V]
  ): Cell[Option[List[TypedReferencedValue[V]]]] = {
    val cm: Cell[CacheMap[V]] = c.cellLoop

    val res: Cell[Option[List[TypedReferencedValue[V]]]] =
      cm.lift(lor, { (x, ro: Option[List[Ref[V]]]) =>
        for {
          r <- ro
          z <- x.resListOfRefs(r)
        } yield (z.toList)
      })
    res
  }

  lazy val streamToSetInitialCacheState =
    new StreamSink[UserMap]()

  implicit val userCache: Cache[User] =
    CacheMaker[User](streamToSetInitialCacheState)
      .getCache()

  implicit val noteCache: Cache[Note] =
    CacheMaker[Note](streamToSetInitialCacheState)
      .getCache()

  implicit val folderCache: Cache[Folder] =
    CacheMaker[Folder](streamToSetInitialCacheState)
      .getCache()

}
