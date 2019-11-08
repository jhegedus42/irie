package app.client.ui.components.mainPages.pages.noteHandling.noteEditor

import app.client.ui.caching.cache.ReadCacheEntryStates
import app.client.ui.caching.cache.comm.read.readCache.ReadCache
import app.client.ui.caching.cache.comm.write.{
  WriteRequestHandlerStates,
  WriteRequestHandlerTC
}
import monocle.macros.Lenses
import app.client.ui.caching.cacheInjector.{
  Cache,
  CacheAndPropsAndRouterCtrl,
  MainPageReactCompWrapper,
  ToBeWrappedMainPageComponent
}
import org.scalajs.dom.File
import org.scalajs.dom._
import app.client.ui.components.mainPages.pages.noteHandling.noteEditor.NoteEditorComp.NoteEditorPage
import app.client.ui.components.mainPages.pages.noteHandling.userNoteList.HelperPrint
import app.client.ui.components.sodium.SodiumWidgets.SodiumLabel
import app.client.ui.components.sodium.{
  SButton,
  STextArea,
  SodiumWidgeSaveEntityToServer
}
import app.client.ui.components.{
  MainPage,
  MainPageInjectedWithCacheAndController
}
import app.shared.comm.postRequests.read.GetLatestEntityByIDReq
import app.shared.comm.postRequests.write.{CreateEntityReq, UpdateReq}
import app.shared.comm.{ReadRequest, WriteRequest}
import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.EntityType
import app.shared.entity.entityValue.values.{Image, Note, User}
import app.shared.entity.refs.{
  RefToEntityByID,
  RefToEntityWithVersion
}
import app.shared.utils.UUID_Utils.EntityIdentity
import io.circe.JsonObject
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.vdom.html_<^.{<, ^, _}
import japgolly.scalajs.react.{
  BackendScope,
  Callback,
  CtorType,
  ScalaComponent
}
import org.scalajs.dom.Node
import org.scalajs.dom.File
import org.scalajs.dom.html.{Anchor, Input}
import io.circe.generic.auto._
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.generic.JsonCodec
import japgolly.scalajs.react.component.ReactForwardRef
import japgolly.scalajs.react.raw.SyntheticEvent
import japgolly.scalajs.react.vdom.Attr
import org.scalajs.dom.raw.EventTarget

import scala.reflect.ClassTag
import scala.scalajs._

trait NoteEditorComp
    extends ToBeWrappedMainPageComponent[NoteEditorComp,
                                         NoteEditorPage] {

  override type PropsT = NoteEditorComp.Props
  override type StateT = String

}

//
//trait EntityCRUD {
//  type V <: EntityType[V]
//
//  def getEntity(
//    c:              Cache,
//    entityIdentity: EntityIdentity[V]
//  )(
//    implicit rc: ReadCache[GetEntityReq[V]],
//    decoder:     Decoder[GetEntityReq[V]#ResT],
//    encoder:     Encoder[GetEntityReq[V]#ParT],
//    ct:          ClassTag[GetEntityReq[V]],
//    ct2:         ClassTag[GetEntityReq[V]#PayLoadT]
//  ): Option[EntityWithRef[V]] = {
//    val refToEntityWithVersion: RefToEntityWithVersion[V] =
//      RefToEntityWithVersion.fromEntityIdentity(entityIdentity)
//
//    val p: GetEntityReq.Par[V] =
//      GetEntityReq.Par(refToEntityWithVersion)
//
//    val r: ReadCacheEntryStates.ReadCacheEntryState[
//      GetEntityReq[V]
//    ] = c.readFromServer[GetEntityReq[V]](p)
//
//    r.toOption.flatMap(x => x.optionEntity)
//  }
//
//}
//
object NoteEditorComp {

  case class NoteEditorPage(uuidFromURL: String)
      extends MainPageInjectedWithCacheAndController[NoteEditorComp,
                                                     NoteEditorPage]

  case class Props(noteID: EntityIdentity[Note])

  class Backend[PropsBE](
    $ : BackendScope[CacheAndPropsAndRouterCtrl[Props], String]) {

    def render(props: CacheAndPropsAndRouterCtrl[Props]) = {

      val p: EntityIdentity[Note]  = props.props.noteID
      val r: RefToEntityByID[Note] = RefToEntityByID(p)
      val e: ReadCacheEntryStates.ReadCacheEntryState[
        GetLatestEntityByIDReq[Note]
      ] =
        props.cache.readFromServer[GetLatestEntityByIDReq[Note]](
          GetLatestEntityByIDReq.Par(r)
        )

      val ee: Option[GetLatestEntityByIDReq.Res[Note]] = e.toOption
      val e2: Option[EntityWithRef[Note]]              = ee.flatMap(_.optionEntity)

      import sodium._
      if (e2.isDefined) {
        val r:      EntityWithRef[Note] = e2.get;
        val titleS: STextArea           = STextArea(r.entityValue.title)
        val button       = SButton()
        val uploadButton = SButton()

        val s = button.sClickedSink.snapshot(titleS.cell)

        s.listen((p: String) => {
          import monocle.macros.syntax.lens._
          val n: Note = r.entityValue.lens(_.title).set(p)
          props.cache.writeToServer[UpdateReq[Note]](
            UpdateReq.UpdateReqPar(r, n)
          )
        })

        import scalatags.JsDom.all._

        var file: Option[File] = None

        case class ImageWriter(n: EntityWithRef[Note]) {

          val writer = props.cache.Writer[CreateEntityReq[Image]]()

          def writeImage(image: String) = {
            val par = CreateEntityReq.CreateEntityReqPar(
              Image("elso", image, Some(r.toRef.entityIdentity))
            )
            writer.writeToServer(par)
          }

        }

        def imageWriter = ImageWriter(r)

        lazy val inputLazy: VdomTagOf[Input] =
          <.input(
            ^.`type` := "file",
            ^.name := "file",
            ^.onChange ==> { x: ^.onChange.Event =>
              {
                val d: js.Dynamic = x.asInstanceOf[js.Dynamic]
                Callback {
                  val file: js.Dynamic = d.target.files.item(0)
                  console.warn("inputLazy onChange", file)
                  val fr: FileReader = new FileReader()
                  val b = file.asInstanceOf[Blob]
                  fr.readAsDataURL(b)
                  import scala.scalajs.js
                  import js.JSConverters._
                  val g = js.Dynamic.global

                  fr.onload = (e) => {
                    console.warn(
                      "file loaded",
                      e.asInstanceOf[js.Dynamic].target.result
                    )
                    g.myX = e.asInstanceOf[js.Dynamic].target.result
                    val s = g.myX.asInstanceOf[String]
                    imageWriter.writeImage(s)
                  }

//                  println(s"bla: $x");

                }
              }
            }
          )

        lazy val formLazy =
          <.form(
            ^.onSubmit ==> { x =>
              Callback {
                println(
                  s"we should submit now the 'thing', which is $x"
                )
                val y = x
                println(y)

              }
            },
            <.label("Upload file:", inputLazy()),
            <.br,
            <.button("Upload", ^.`type` := "submit")
          )

        <.div(
          titleS.component(),
          button.getVDOM(),
          "For debug:",
          <.br,
          <.pre(HelperPrint.prettyPrint(e2)),
          formLazy,
          "---------------------------------",
          <.br,
          "result: ",
          SodiumLabel(
            imageWriter.writer.getStream
              .map(
                x =>
                  x.getRes.map(
                    x =>
                      s"${x.entity.entityValue.title} ${x.entity.entityValue.reference}"
                  )
              ).hold(
                None
              ).map(_.toString)
          ).comp()
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
