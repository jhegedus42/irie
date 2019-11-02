package app.client.ui.components.mainPages.helpers

import app.client.ui.caching.cache.comm.read.ReadCache
import app.client.ui.caching.cacheInjector.CacheAndPropsAndRouterCtrl
import app.shared.comm.ReadRequest
import app.shared.comm.postRequests.GetEntityReq
import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.EntityType
import app.shared.entity.refs.RefToEntityWithVersion
import io.circe.{Decoder, Encoder}
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.TagMod
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^.{<, ^, _}
import japgolly.scalajs.react.vdom.{VdomElement, html_<^}
import japgolly.scalajs.react.{BackendScope, Callback, CtorType, ScalaComponent}

import scala.reflect.ClassTag

case class ListRenderHelper[T <: EntityType[T], Props](
  cacheAndPropsAndRouterCtrl: CacheAndPropsAndRouterCtrl[Props]
)(
  implicit c: ReadCache[GetEntityReq[T]],
  d:          Decoder[GetEntityReq[T]#ResT],
  e:          Encoder[GetEntityReq[T]#ParT],
  ct:         ClassTag[GetEntityReq[T]#PayLoadT]) {

  def ref2EntityOption(
    r: RefToEntityWithVersion[T]
  ): Option[EntityWithRef[T]] = {
    val par_ = GetEntityReq.Par(r)
    val res_ : Option[GetEntityReq.Res[T]] =
      cacheAndPropsAndRouterCtrl.cache
        .readFromServer[GetEntityReq[T]](par_)
        .toOption
    val emptyResult: GetEntityReq.Res[T] =
      GetEntityReq.Res[T](None)

    res_.getOrElse(emptyResult).optionEntity
  }

  def getEntityListAsVDOM(
    l: List[Option[EntityWithRef[T]]],
    g: Option[EntityWithRef[T]] => VdomElement
  ): TagMod = {
    TagMod(l.map(g).toVdomArray)
  }
}
