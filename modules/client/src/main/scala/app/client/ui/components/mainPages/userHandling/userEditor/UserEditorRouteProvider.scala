package app.client.ui.components.mainPages.userHandling.userEditor

import app.client.ui.caching.cacheInjector.{Cache, MainPageReactCompWrapper}
import app.client.ui.components.MainPage
import app.client.ui.components.mainPages.userHandling.userEditor.UserEditorComp.UserEditorPage
import app.shared.utils.UUID_Utils.EntityIdentity

object UserEditorRouteProvider {

  def getRoute(cacheInterface: Cache) = {

    import japgolly.scalajs.react.extra.router._

    dsl: RouterConfigDsl[MainPage] =>
      import dsl._

      def c(
        page: UserEditorPage,
        ctl:  RouterCtl[MainPage]
      ): MainPageReactCompWrapper[UserEditorComp, UserEditorPage] =
        MainPageReactCompWrapper[UserEditorComp, UserEditorPage](
          cache = cacheInterface,
          propsProvider = () =>
            UserEditorComp.Props(EntityIdentity(page.paramFromURL),
                                 ctl),
          comp = UserEditorComp.component,
          routerController = ctl
        )

      def g2(t2: (UserEditorPage, RouterCtl[MainPage])) =
        c(t2._1, t2._2).wrappedConstructor

      def h2: UserEditorPage => dsl.Renderer =
        p => Renderer(rc => g2((p, rc)))

      def f: StaticDsl.RouteB[UserEditorPage] =
        "#userEditorPageRoute" / string("[a-z-A-Z0-9]+")
          .caseClass[UserEditorPage]

      dynamicRouteCT[UserEditorPage](f).~>(h2)

  }
}
