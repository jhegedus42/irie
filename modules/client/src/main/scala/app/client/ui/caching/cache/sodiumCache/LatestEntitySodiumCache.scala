package app.client.ui.caching.cache.sodiumCache

import app.client.ui.caching.cache.ReadCacheEntryStates.Returned
import app.client.ui.caching.cache.comm.AJAXCalls
import app.client.ui.caching.cache.comm.AJAXCalls.{AjaxCallPar, sendPostAjaxRequest}
import app.client.ui.caching.cacheInjector.ReRenderer
import app.shared.comm.sodium.SodiumRouteName
import app.shared.comm.{PostRequest, RouteName}
import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.EntityType
import app.shared.utils.UUID_Utils.EntityIdentity
import org.scalajs.dom.ext.Ajax
import sodium._

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.Try

object AJAXModul{


  val routeName: SodiumRouteName= ???

  val url: String = routeName.name

  import io.circe.parser.decode
  import io.circe.syntax._


  val headers: Map[String, String] = Map(
    "Content-Type" -> "application/json"
  )

  val res1: Future[] =
    Ajax
      .post(url, json_line, headers = headers)
      .map(_.responseText)
      .map((x: String) => {
        decode[Req#ResT](x)
      })
      .map(x => x.right.get)


}

case class EntityCreator[V<:EntityType[V]](){

  val entityInsertedStream = ???

}


trait SodiumEntityCache[V<:EntityType[V]] {

  val entityUpdatedStream = ???

  val collectionChangedStream = ???

  def update(ref:EntityWithRef[V]) = {
      ???
  }

  def fillUp(s:Set[Value]) = ???

  def insert(v:V) : Unit ={
    val key = ???
    val value = ???
  }

  type Key=EntityIdentity[V]
  type Value=EntityWithRef[V]
  val initMap=Map[Key,Value]()

  val cellMapLoop = new CellLoop[Map[Key,Value]]()

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue



}
