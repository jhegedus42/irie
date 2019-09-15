package app.client.ui.components.router

import app.client.ui.caching.cacheInjector.{CacheInterface, ReactCompWrapper}
import app.client.ui.components.generalComponents.TopNavComp.Menu
import app.client.ui.components.generalComponents.{FooterComp, TopNavComp}
import app.client.ui.components.router.mainPageComponents.adminPage.StaticAdminPage
import app.client.ui.components.router.mainPageComponents.sumNumbers.{SumNumbersComponent, SumNumbersPage}
import app.client.ui.components.router.mainPageComponents._
import app.client.ui.components.router.mainPageComponents.sumNumbers.SumNumbersPage.SumNumbersProps
import app.client.ui.components.router.mainPageComponents.userEditor.UserEditorPageComp
import japgolly.scalajs.react.extra.router.{Resolution, RouterConfigDsl, RouterCtl, _}
import japgolly.scalajs.react.vdom.html_<^._

// this wrapper is needed so that we can "re render the react tree below this"
// it gets the re render triggerer so that it can update-it

object Pages {

  def itemPage = { dsl: RouterConfigDsl[MainPage] =>
    import dsl._

    val _itemPage = japgolly.scalajs.react.ScalaComponent
      .builder[ItemPage]("Item page")
      .render(p => <.div(s"Info for item #${p.props.id}"))
      .build

    dynamicRouteCT("item" / int.caseClass[ItemPage]) ~> (dynRender(
      _itemPage(_: ItemPage)
    ))

  }

  def adminPage = { dsl: RouterConfigDsl[MainPage] =>
    import dsl._
    val adminPage
      : dsl.Rule = staticRoute("#admin", AdminPage) ~> render(
      StaticAdminPage.component()
    )

    adminPage
  }

//  def _tmp_userEditorPage(cacheInterface: CacheInterface) = {
//    dsl: RouterConfigDsl[MainPage] =>
//      import dsl._
//
//      val _userEditorPage = japgolly.scalajs.react.ScalaComponent
//        .builder[UserEditorPage]("User editor page")
//        .render(p => <.div(s"Info for user #${p.props.uuid}"))
//        .build
//
//      // todo-now-5 factor this out and use it to get info on user
//
//      // todo-now-6 "inject da cache",
//      //  see "sumNumberCompRoute" above - for inspiration
//
//      //      dynamicRouteCT(
//      //        "#app" / "user" / string("[a-zA-Z]+")
//      //          .caseClass[UserEditorPage]
//      //      ) ~> (dynRender(
//      //        _userEditorPage(_: UserEditorPage)
//      //      ))
//
//      dynamicRouteCT(
//        "#app" / "user" / string("[a-zA-Z]+")
//          .caseClass[UserEditorPage]
//      ) ~> (dynRender({ paramForUserEditorPage: UserEditorPage =>
//        //        _userEditorPage(paramForUserEditorPage)
//
//        SumNumbersPage.getWrappedReactCompConstructor(
//          cacheInterface,
//          () =>
//            SumNumbersProps(
//              s"hello world 42 + ${paramForUserEditorPage.uuid}"
//            )
//        )
//
//      }))
//
//    // this is a little "trick" here ... we want to see if we can pass
//    //  some props into the "cache injected component" from the URL
//
//    //  todo-now-7 : misuse SumNumbersPage to display user info.
//    //
//    //   Details :
//    //
//    //   Use the props from the URL to provide an uuid to an user
//    //   so that it can be fetched ... and it's name and favorite
//    //   number can be displayed.
//    //
//    //
//    //
//
//    //
//    //
//    //
//    // todo-now-8 : display all user Refs, somewhere ...
//    //
//    //
//    //
//    //
//
//  }

}

case class RouterComp() {

  lazy val cache = new CacheInterface()

  // todo-later factor out the wrapping , as a start for
  //   "sumNumberCompRoute" below

  val config = RouterConfigDsl[MainPage].buildConfig {
    dsl: RouterConfigDsl[MainPage] =>
      import dsl._

      val loginRoute: dsl.Rule = staticRoute(root, LoginPage) ~> render(
        LoginPageConstructor.component()
      )

      val sumNumberCompRoute: dsl.Rule = {
        staticRoute("#cacheTest", SumIntDemo) ~>
          render({
            SumNumbersPage.getWrappedReactCompConstructor(
              cache,
              () => SumNumbersProps("hello world 42")
            )
          })
      }


      (trimSlashes
        | loginRoute
        | sumNumberCompRoute
        | Pages.itemPage(dsl)
        | UserEditorPageComp.getRoute(cache)(dsl)
        | Pages.adminPage(dsl))
        .notFound(
          redirectToPage(LoginPage)(Redirect.Replace)
        )
        .renderWith(f = layout)
  }

  val mainMenu = Vector.apply(
    Menu.apply("Home", LoginPage),
    Menu.apply("SumIntDemo", SumIntDemo),
    Menu.apply("User Editor", UserEditorPage),
    Menu.apply("ItemPage 4", ItemPage(4)),
    Menu.apply("ItemPage 42", ItemPage(42)),
    Menu.apply("Admin Page", AdminPage)
  )

  val baseUrl = BaseUrl.fromWindowOrigin_/

  val router = Router.apply(baseUrl, config)

  // todo-soon ^^^ this router should be wrapped into something
  //  "stateful", so that we can trigger a re-render on it
  //  then we do not need these stupid wrappers for our pages
  //  with cache... just to re-render them...
  //  whatever needs the cache will either get it as prop

  def layout(
    c: RouterCtl[MainPage],
    r: Resolution[MainPage]
  ) = {
    <.div.apply(
      TopNavComp.apply(TopNavComp.Props.apply(mainMenu, r.page, c)),
      r.render.apply(),
      FooterComp()
    )
  }

}
