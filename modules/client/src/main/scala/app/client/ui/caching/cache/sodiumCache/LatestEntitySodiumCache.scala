package app.client.ui.caching.cache.sodiumCache

import app.client.ui.caching.cache.ReadCacheEntryStates.Returned
import app.client.ui.caching.cache.comm.AJAXCalls
import app.client.ui.caching.cache.comm.AJAXCalls.{AjaxCallPar, sendPostAjaxRequest}
import app.client.ui.caching.cacheInjector.ReRenderer
import app.shared.comm.sodium.{ClassTagPrivoders, SodiumCRUDReq, SodiumParamConverterImpl, SodiumRouteName, SodiumRouteNameProvider}

import scala.reflect.ClassTag
import app.shared.comm.{PostRequest, RouteName}
import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.EntityType
import app.shared.utils.UUID_Utils.EntityIdentity
import org.scalajs.dom.ext.Ajax
import sodium._

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.Try


trait AJAXModul[RT <: SodiumCRUDReq[V], V <: EntityType[V]] {
  self: SodiumParamConverterImpl[RT, V] with ClassTagPrivoders[RT,V]=>

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

//  implicit def ct: ClassTag[RT]
//  implicit def e:  ClassTag[V]

  def url: String = SodiumRouteNameProvider.getRouteName[V, RT]().name

  import io.circe.parser.decode
  import io.circe.syntax._

  val headers: Map[String, String] = Map(
    "Content-Type" -> "application/json"
  )

  val ajaxCall: String => Future[String] = json_line =>
    Ajax
      .post(url, json_line, headers = headers)
      .map(_.responseText)

  val query: RT#Par => Future[RT#Resp] = ajaxCall(_).map(stringToResp)

}

case class EntityCreator[V <: EntityType[V]]() {

  val entityInsertedStream = ???

}

trait SodiumEntityCache[V <: EntityType[V]] {

  val entityUpdatedStream = ???

  val collectionChangedStream = ???

  def update(ref: EntityWithRef[V]) = {
    ???
  }

  def fillUp(s: Set[Value]) = ???

  def insert(v: V): Unit = {
    val key   = ???
    val value = ???
  }

  type Key   = EntityIdentity[V]
  type Value = EntityWithRef[V]
  val initMap = Map[Key, Value]()

  val cellMapLoop = new CellLoop[Map[Key, Value]]()

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

}
