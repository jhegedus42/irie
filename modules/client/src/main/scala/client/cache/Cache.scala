package client.cache

import client.sodium.core.{
  CellLoop,
  Stream,
  StreamSink,
  Transaction
}
import client.ui.login.UserLoginStatusHandler
import dataStorage.{TypedReferencedValue, User, Value}
import dataStorage.stateHolder.UserMap
import shapeless.Typeable

//object CacheProvider {}

import client.sodium.core.Cell
import dataStorage.Value

case class CacheInserter()

case class Cache[V <: Value[V]](
  transformerConstructor: Stream[
    CacheMap[V] => CacheMap[V]
  ],
  typeName: String
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
    rv: TypedReferencedValue[V]
  )(
    implicit
    typeable: Typeable[V]
  ): CacheMap[V] => CacheMap[V] =
    CacheMap.insertReferencedValue[V](rv)

  val inserterStream: StreamSink[TypedReferencedValue[V]] =
    new StreamSink[TypedReferencedValue[V]]()

  //  todo-now
  //   1. make inserting new user work

  //  todo-now
  //     1.1. listen to this stream ^^^
  //     and send updates to the server, to mirror the changes made on
  //     the client

  //  todo-now
  //     1.2. show status of "syncing" / "synced" somewhere on the
  //     console / screen (can be even a state in a Cell, later)

  val ins1
    : Stream[TypedReferencedValue[V]] = inserterStream.map(
    _.addTypeInfo().addEntityOwnerInfo(
      UserLoginStatusHandler.getUserLoginStatusDev.userOption.get.ref
    )
  )

  private def ins2: Stream[CacheMap[V] => CacheMap[V]] =
    ins1.map(inserter)

  ins1.listen(
    (x: TypedReferencedValue[V]) =>
      println(
        s"here we should send an AJAX request to insert this new" +
          s"value into the servers data store: $x"
      )
    // todo-now CONTINUE THIS NOW
    //  1.1.1 launch AJAX request to
    //  insert/create TypedReferencedValue[V]
    //  on the server, too
  )

  val transformer: Stream[CacheMap[V] => CacheMap[V]] =
    transformerConstructor.orElse(ins2)

  val cellLoop = Transaction.apply[CellLoop[CacheMap[V]]](
    { _ =>
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
    CacheMaker(streamToSetInitialCacheState).getCache()

  //  val note:  Cache[Note]  = CacheMaker(s).makeCache[Note]()
  //  val image: Cache[Image] = CacheMaker(s).makeCache[Image]()

}
