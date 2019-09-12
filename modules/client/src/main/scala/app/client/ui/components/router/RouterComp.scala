package app.client.ui.components.router

import app.client.ui.caching.cacheInjector.{
  CacheInterface,
  ReactCompWrapper
}
import app.client.ui.components.generalComponents.TopNavComp.Menu
import app.client.ui.components.generalComponents.{
  FooterComp,
  TopNavComp
}
import app.client.ui.components.router.mainPageComponents.adminPage.StaticAdminPage
import app.client.ui.components.router.mainPageComponents.sumNumbers.{
  SumNumbersComponent,
  SumNumbersPage
}
import app.client.ui.components.router.mainPageComponents._
import app.client.ui.components.router.mainPageComponents.sumNumbers.SumNumbersPage.SumNumbersProps
import japgolly.scalajs.react.extra.router.{
  Resolution,
  RouterConfigDsl,
  RouterCtl,
  _
}
import japgolly.scalajs.react.vdom.html_<^._

// this wrapper is needed so that we can "re render the react tree below this"
// it gets the re render triggerer so that it can update-it

case class RouterComp() {

  lazy val cache = new CacheInterface()

  // todo-later factor out the wrapping , as a start for
  //   "sumNumberCompRoute" below

  val config = RouterConfigDsl[MainPage].buildConfig { dsl =>
    import dsl._

    val homeRoute: dsl.Rule = staticRoute(root, HomePage) ~> render(
      StaticPageExample.apply()
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

    val adminPage
      : dsl.Rule = staticRoute("#admin", AdminPage) ~> render(
      StaticAdminPage.apply()
    )

    val userEditorPage: dsl.Rule = {

      val _userEditorPage = japgolly.scalajs.react.ScalaComponent
        .builder[UserEditorPage]("User editor page")
        .render(p => <.div(s"Info for user #${p.props.uuid}"))
        .build

      // todo-now-5 factor this out and use it to get info on user

      // todo-now-6 "inject da cache",
      //  see "sumNumberCompRoute" above - for inspiration

//      dynamicRouteCT(
//        "#app" / "user" / string("[a-zA-Z]+")
//          .caseClass[UserEditorPage]
//      ) ~> (dynRender(
//        _userEditorPage(_: UserEditorPage)
//      ))

      dynamicRouteCT(
        "#app" / "user" / string("[a-zA-Z]+")
          .caseClass[UserEditorPage]
      ) ~> (dynRender({ paramForUserEditorPage: UserEditorPage =>
//        _userEditorPage(paramForUserEditorPage)

        SumNumbersPage.getWrappedReactCompConstructor(
          cache,
          () =>
            SumNumbersProps(
              s"hello world 42 + ${paramForUserEditorPage.uuid}"
            )
        )

      }))
      // this is a little "trick" here ... we want to see if we can pass
      //  some props into the "cache injected component" from the URL

    }

    val itemPage: dsl.Rule = {

      val _itemPage = japgolly.scalajs.react.ScalaComponent
        .builder[ItemPage]("Item page")
        .render(p => <.div(s"Info for item #${p.props.id}"))
        .build

      dynamicRouteCT("item" / int.caseClass[ItemPage]) ~> (dynRender(
        _itemPage(_: ItemPage)
      ))

    }

    (trimSlashes
      | homeRoute
      | sumNumberCompRoute
      | itemPage
      | userEditorPage
      | adminPage)
      .notFound(
        redirectToPage(HomePage)(Redirect.Replace)
      )
      .renderWith(f = layout)
  }

  val mainMenu = Vector.apply(
    Menu.apply("Home", HomePage),
    Menu.apply("SumIntDemo", SumIntDemo),
    Menu.apply("ItemPage 4", ItemPage(4)),
    Menu.apply("User Editor Page Geza", UserEditorPage("Geza")),
    Menu.apply("ItemPage 42", ItemPage(42)),
    Menu.apply("Admin Page", AdminPage)
  )
  val baseUrl = BaseUrl.fromWindowOrigin_/

  val router =
    Router.apply(baseUrl, config)

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
