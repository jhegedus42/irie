package app.client.ui.caching.cache.sodiumCache

import app.client.ui.caching.cache.ReadCacheEntryStates.Returned
import app.client.ui.caching.cache.comm.AJAXCalls
import app.client.ui.caching.cache.comm.AJAXCalls.{
  AjaxCallPar,
  sendPostAjaxRequest
}
import app.client.ui.caching.cacheInjector.ReRenderer
import app.shared.comm.sodium.{
  ClassTagPrivoders,
  SodiumCRUDReq,
  SodiumParamConverters,
  SodiumRouteName,
  SodiumRouteNameProvider
}

import scala.reflect.ClassTag
import app.shared.comm.{PostRequest, RouteName}
import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.EntityType
import app.shared.entity.entityValue.values.User
import app.shared.utils.UUID_Utils.EntityIdentity
import org.scalajs.dom.ext.Ajax
import sodium._

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.Try
//import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.generic.JsonCodec
import io.circe.parser._
import io.circe.{Decoder, Error, _}

//case class EntityCreator[V <: EntityType[V]]() {
//
//  val entityInsertedStream = ???
//
//}

trait SodiumEntityCache[V <: EntityType[V]] {

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

  type Key   = EntityIdentity[V]
  type Value = EntityWithRef[V]
  val initMap = Map[Key, Value]()
  type CellMap=Map[Key,Value]

//  val cellMapLoop = new CellLoop[Map[Key, Value]]()

//  val cell = new Cell[Map[Key, Value]](initMap)

  val streamSink = new StreamSink[CellMap]()
  val cell = streamSink.hold(initMap)

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

}

object SodiumEntityCache {
  implicit val userCache: SodiumEntityCache[User] =
    new SodiumEntityCache[User] {}
}
