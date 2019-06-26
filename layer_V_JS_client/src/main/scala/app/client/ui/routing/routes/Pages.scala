package app.client.ui.routing.routes

object Pages {

  // mi a Page es az URL kozott a kulnombseg ?

  // nem biztos h. ez egy jo kerdes


  //AROP abstract repr of page

  sealed trait AbstrReprOfPage

  case object HomePage_AbstrReprOfPage extends AbstrReprOfPage
  case object CacheTest_AbstrReprOfPage extends AbstrReprOfPage
  case class Items_AbstrReprOfPage(p:     Item ) extends AbstrReprOfPage
  case class Item_AbstrReprOfPage(id: Int ) extends AbstrReprOfPage
}
