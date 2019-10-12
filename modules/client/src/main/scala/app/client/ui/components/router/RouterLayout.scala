package app.client.ui.components.router

import app.client.ui.components.MainPage
import app.client.ui.components.mainPageLayout.{FooterComp, TopNavComp}
import japgolly.scalajs.react.extra.router.{Resolution, RouterCtl}
import japgolly.scalajs.react.vdom.TagOf
import japgolly.scalajs.react.vdom.html_<^.<
import org.scalajs.dom.html.Div

object RouterLayout{
  def layout(
              c: RouterCtl[MainPage],
              r: Resolution[MainPage]
            ): TagOf[Div] = {

    def tnc =
      TopNavComp.apply(TopNavComp.Props.apply( r.page, c))

    <.div.apply(
      tnc,
      r.render.apply() //,
//      FooterComp()
    )
  }

}
