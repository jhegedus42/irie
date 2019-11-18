package client.cache

import client.sodium.core.{CellLoop, Stream, StreamSink, Transaction}
import dataStorage.{ReferencedValue, User, Value}
import dataStorage.stateHolder.UserMap

//object CacheProvider {}

import client.sodium.core.Cell
import dataStorage.Value

case class Cache[V <: Value[V]](
  transformerConstructor: Stream[CacheMap[V] => CacheMap[V]],
  typeName:               String) {

//  val updateStarter: StreamSink[Unit] = new StreamSink[Unit]()

  private def inserter(rv: ReferencedValue[V]): CacheMap[V] => CacheMap[V] =
    CacheMap.insertReferencedValue[V](rv)

  val inserterStream: StreamSink[ReferencedValue[V]] =
    new StreamSink[ReferencedValue[V]]()

//  def inserterStreamTransformer(
//    s: ReferencedValue[V]
//  ): Stream[CacheMap[V] => CacheMap[V]] = s.map(r => inserter(r))
//
  private def ins1: Stream[CacheMap[V] => CacheMap[V]] =
    inserterStream.map(inserter)

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
