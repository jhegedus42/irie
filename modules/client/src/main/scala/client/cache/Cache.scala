package client.cache

import client.sodium.core.{CellLoop, Stream, StreamSink, Transaction}
import client.ui.login.UserLoginStatusHandler
import dataStorage.{ReferencedValue, User, Value}
import dataStorage.stateHolder.UserMap
import shapeless.Typeable

//object CacheProvider {}

import client.sodium.core.Cell
import dataStorage.Value

case class Cache[V <: Value[V]](
  transformerConstructor: Stream[CacheMap[V] => CacheMap[V]],
  typeName:               String
)(
  implicit
  typeable: Typeable[V]) {

//  val updateStarter: StreamSink[Unit] = new StreamSink[Unit]()

  /**
    *
    * We assume here that alice is the only user, hence we use:
    * `getUserLoginStatusDev` for defining the owning User.
    *
    * @param rv
    * @param typeable
    * @return
    */
  private def inserter(
    rv: ReferencedValue[V]
  )(
    implicit
    typeable: Typeable[V]
  ): CacheMap[V] => CacheMap[V] =
    CacheMap.insertReferencedValue[V](
      rv.addTypeInfo().addEntityOwnerInfo(
          UserLoginStatusHandler.getUserLoginStatusDev.userOption.get.ref
        )
    )

  val inserterStream: StreamSink[ReferencedValue[V]] =
    new StreamSink[ReferencedValue[V]]()

  // todo-now =>
  //  1. listen to this stream ^^^
  //     and send updates to the server, to mirror the changes made on
  //     the client
  //  2. show status of "syncing" / "synced" somewhere on the
  //     console / screen (can be even a state in a Cell, later)

  private def ins1: Stream[CacheMap[V] => CacheMap[V]] =
    inserterStream.map(inserter)

  ins1.listen(
    x =>
      println(
        s"here we should send an AJAX request to insert this new" +
          s"value into the servers data store: $x"
      )
  )

  val transformer: Stream[CacheMap[V] => CacheMap[V]] =
    transformerConstructor.orElse(ins1)

  val cellLoop = Transaction.apply[CellLoop[CacheMap[V]]](
    { _ =>
      lazy val afterUpdate: Stream[CacheMap[V]] =
        transformer.snapshot(
          counterValue, { (f: CacheMap[V] => CacheMap[V], c: CacheMap[V]) =>
            f(c)
          }
        )

      lazy val counterValue: CellLoop[CacheMap[V]] = new CellLoop[CacheMap[V]]()

      counterValue.loop(afterUpdate.hold(CacheMap[V]()))

      counterValue
    }
  )

}

object Cache {
  lazy val streamToSetInitialCacheState = new StreamSink[UserMap]()

  implicit val user: Cache[User] =
    CacheMaker(streamToSetInitialCacheState).getCache()

  //  val note:  Cache[Note]  = CacheMaker(s).makeCache[Note]()
  //  val image: Cache[Image] = CacheMaker(s).makeCache[Image]()

}
