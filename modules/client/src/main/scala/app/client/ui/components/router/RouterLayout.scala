package app.client.ui.components.router

import app.client.ui.components.MainPage
import app.client.ui.components.mainPageLayout.{FooterComp, TopNavComp}
import japgolly.scalajs.react.extra.router.{Resolution, RouterCtl}
import japgolly.scalajs.react.vdom.html_<^.<

object RouterLayout{
  def layout(
              c: RouterCtl[MainPage],
              r: Resolution[MainPage]
            ) = {
    println("layout was called")

    // todo later
    //   refresher = () => c.refresh.runNow()
    //   this is a huge hack ... todo-later - "fix it"
    //   this is here so that we can re-render the router when a user logs in

    // todo-later
    //  wait for gitter channal to try to answer a question on
    //  how to implement the "login" use case - using this router

    val tnc =
      TopNavComp.apply(TopNavComp.Props.apply( r.page, c))

    <.div.apply(
      tnc,
      r.render.apply(),
      FooterComp()
    )
  }

}
