package client.cache

import client.sodium.core
import client.sodium.core.{Cell, StreamSink}
import dataStorage.{Ref, ReferencedValue, User, Value}
import io.circe.generic.JsonCodec
import io.circe.syntax._

import scala.concurrent.ExecutionContextExecutor

//case class EntityCreator[V <: EntityType[V]]() {
//
//  val entityInsertedStream = ???
//
//}

trait SodiumEntityCache[V <: Value[V]] {

//  val entityUpdatedStream = ???
//
//  val collectionChangedStream = ???
//
//  def update(ref: EntityWithRef[V]) = {
//    ???
//  }
//
//
//  def insert(v: V): Unit = {
//    val key   = ???
//    val value = ???
//  }
//  def fillUp(s: Set[Value]) =

  type Key   = Ref[V]
  type Value = ReferencedValue[V]
  val initMap = Map[Key, Value]()
  type CellMap = Map[Key, Value]

//  val cellMapLoop = new CellLoop[Map[Key, Value]]()

//  val cell = new Cell[Map[Key, Value]](initMap)

  val cellHoldStream = new Cell(new core.Stream[Unit])

  val loadMapFromServer = new StreamSink[Unit]()

  def loadFromServer(trigger: core.Stream[Unit]): Unit = {
    //continue here
  }

  val streamSink = new StreamSink[CellMap]()

//  loadMapFromServer.listen( /// todo now ... )

  val cell = streamSink.hold(initMap)

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  val streamUpdater = new StreamSink[core.Stream[Unit]]()

  val streamHolderCell = streamUpdater.hold(new core.Stream[Unit]())

  val latestStream: core.Stream[Unit] = Cell.switchS(streamHolderCell)

  latestStream.listen(x => println("button has fired"))

}

object SodiumEntityCache {
  implicit val userCache: SodiumEntityCache[User] =
    new SodiumEntityCache[User] {}
}
