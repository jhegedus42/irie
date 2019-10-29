package app.client.ui.components.mainPages.noteHandling.noteEditor

import app.client.ui.caching.cache.ReadCacheEntryStates
import app.client.ui.caching.cacheInjector.{
  Cache,
  CacheAndPropsAndRouterCtrl,
  MainPageReactCompWrapper,
  ToBeWrappedMainPageComponent
}
import app.client.ui.components.mainPages.noteHandling.noteEditor.NoteEditorComp.NoteEditorPage
import app.client.ui.components.{
  MainPage,
  MainPageInjectedWithCacheAndController,
  StaticTemplatePage
}
import app.shared.comm.ReadRequest
import app.shared.comm.postRequests.GetEntityReq
import app.shared.entity.entityValue.values.{Note, User}
import app.shared.entity.refs.RefToEntityWithVersion
import app.shared.utils.UUID_Utils.EntityIdentity
import japgolly.scalajs.react.component.Scala.Component
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import japgolly.scalajs.react.{BackendScope, CtorType, ScalaComponent}
import org.scalajs.dom.html.Anchor

trait NoteEditorComp
    extends ToBeWrappedMainPageComponent[NoteEditorComp,
                                         NoteEditorPage] {

  override type PropsT = NoteEditorComp.Props
  override type StateT = String

}

object NoteEditorComp {

  case class NoteEditorPage(uuidFromURL: String)
      extends MainPageInjectedWithCacheAndController[NoteEditorComp,
                                                     NoteEditorPage]

  case class Props(noteID: EntityIdentity[Note])

  class Backend[PropsBE](
    $ : BackendScope[CacheAndPropsAndRouterCtrl[Props], String]) {

    def render(x: CacheAndPropsAndRouterCtrl[Props]) = {
      //val

      val refToEntityWithVersion: RefToEntityWithVersion[Note] =
        RefToEntityWithVersion.fromEntityIdentity(x.props.noteID)
      // CONTINUE HERE

      val p: GetEntityReq.Par[Note] =
        GetEntityReq.Par(refToEntityWithVersion)
      val r: ReadCacheEntryStates.ReadCacheEntryState[
        ReadRequest,
        GetEntityReq[Note]
      ] = x.cache.readFromServer[ReadRequest, GetEntityReq[Note]](p)
//      <.div(x.props.toString, "  ")
      r.toString
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
  // todo-now CONTINUE HERE
  //  step 1
  //  print uuidFromURL
  //  step 2
  //  navigate to this page from the note list comp using a link
  //  step 3
  //  print details for the note whose UUID is received from the NoteEditorPage

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
