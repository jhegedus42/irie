package app.client.ui.components.mainPages.pages.noteHandling.noteEditor

import app.client.ui.caching.cache.ReadCacheEntryStates
import app.client.ui.caching.cache.comm.read.readCache.ReadCache
import app.client.ui.caching.cache.comm.write.{WriteRequestHandlerStates, WriteRequestHandlerTC}
import monocle.macros.Lenses
import app.client.ui.caching.cacheInjector.{Cache, CacheAndPropsAndRouterCtrl, MainPageReactCompWrapper, ToBeWrappedMainPageComponent}
import app.client.ui.components.mainPages.pages.noteHandling.noteEditor.NoteEditorComp.NoteEditorPage
import app.client.ui.components.sodium.{SButton, STextArea, SodiumWidgeSaveEntityToServer}
import app.client.ui.components.{MainPage, MainPageInjectedWithCacheAndController}
import app.shared.comm.{ReadRequest, WriteRequest}
import app.shared.comm.postRequests.{GetEntityReq, UpdateReq}
import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.EntityType
import app.shared.entity.entityValue.values.{Note, User}
import app.shared.entity.refs.RefToEntityWithVersion
import app.shared.utils.UUID_Utils.EntityIdentity
import io.circe.{Decoder, Encoder}
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import japgolly.scalajs.react.{BackendScope, CtorType, ScalaComponent}
import org.scalajs.dom.Node
import org.scalajs.dom.html.Anchor
import io.circe.generic.auto._
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.generic.JsonCodec
import monocle.macros.syntax.lens._

import scala.reflect.ClassTag

trait NoteEditorComp
    extends ToBeWrappedMainPageComponent[NoteEditorComp,
                                         NoteEditorPage] {

  override type PropsT = NoteEditorComp.Props
  override type StateT = String

}

trait EntityCRUD {
  type V <: EntityType[V]

  def getEntity(
    c:              Cache,
    entityIdentity: EntityIdentity[V]
  )(
    implicit rc: ReadCache[GetEntityReq[V]],
    decoder:     Decoder[GetEntityReq[V]#ResT],
    encoder:     Encoder[GetEntityReq[V]#ParT],
    ct:          ClassTag[GetEntityReq[V]],
    ct2:         ClassTag[GetEntityReq[V]#PayLoadT]
  ): Option[EntityWithRef[V]] = {
    val refToEntityWithVersion: RefToEntityWithVersion[V] =
      RefToEntityWithVersion.fromEntityIdentity(entityIdentity)

    val p: GetEntityReq.Par[V] =
      GetEntityReq.Par(refToEntityWithVersion)

    val r: ReadCacheEntryStates.ReadCacheEntryState[
      GetEntityReq[V]
    ] = c.readFromServer[GetEntityReq[V]](p)

    r.toOption.flatMap(x => x.optionEntity)
  }

}

object NoteEditorComp {

  case class NoteEditorPage(uuidFromURL: String)
      extends MainPageInjectedWithCacheAndController[NoteEditorComp,
                                                     NoteEditorPage]

  case class Props(noteID: EntityIdentity[Note])

  class Backend[PropsBE](
    $ : BackendScope[CacheAndPropsAndRouterCtrl[Props], String]) {

    object VDOM extends EntityCRUD {
      override type V = Note
    }

    def render(x: CacheAndPropsAndRouterCtrl[Props]) = {

      val e: Option[EntityWithRef[Note]] =
        VDOM.getEntity(x.cache, x.props.noteID)

      import sodium._
      if (e.isDefined) {
        val r:      EntityWithRef[Note] = e.get;
        val titleS: STextArea           = STextArea(r.entityValue.title)
        val button = SButton()

        val s = button.sClickedSink.snapshot(titleS.cell)

        s.listen( (p: String) =>{
          val n: Note =r.entityValue.lens(_.title).set(p)
          x.cache.writeToServer[UpdateReq[Note]](UpdateReq.UpdateReqPar(r,n))
        })


        <.div(
          titleS.component(),
          button.getVDOM()
        )

      } else
        <.div("Loading...")

    }

  }

  val component
    : Component[CacheAndPropsAndRouterCtrl[Props], String, Backend[
      Props
    ], CtorType.Props] =
    ScalaComponent
      .builder[CacheAndPropsAndRouterCtrl[Props]]("Note Editor")
      .initialState("hello")
      .renderBackend[Backend[Props]]
      .build

//    .

}

object NoteEditorRouteProvider {

  def getRoute(cacheInterface: Cache) = {

    import japgolly.scalajs.react.extra.router._

    dsl: RouterConfigDsl[MainPage] => {
      import dsl._

      def c(
        page: NoteEditorPage,
        ctl:  RouterCtl[MainPage]
      ): MainPageReactCompWrapper[NoteEditorComp, NoteEditorPage] =
        MainPageReactCompWrapper[NoteEditorComp, NoteEditorPage](
          cache = cacheInterface,
          propsProvider = _ =>
            NoteEditorComp.Props(EntityIdentity(page.uuidFromURL)),
          comp             = NoteEditorComp.component,
          routerController = ctl
        )

      def g2(t2: (NoteEditorPage, RouterCtl[MainPage])) =
        c(t2._1, t2._2).wrappedConstructor

      def h2: NoteEditorPage => dsl.Renderer =
        p => Renderer(rc => g2((p, rc)))

      def f: StaticDsl.RouteB[NoteEditorPage] =
        "#NoteEditorPageRoute" / string("[a-z-A-Z0-9]+")
          .caseClass[NoteEditorPage]

      dynamicRouteCT[NoteEditorPage](f).~>(h2)
    }

  }
}
