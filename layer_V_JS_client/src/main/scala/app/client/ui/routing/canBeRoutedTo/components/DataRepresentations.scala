package app.client.ui.routing.canBeRoutedTo.components


object DataRepresentations {


  sealed trait AbstrReprOfPage

  case object HomePage_AbstrReprOfPage extends AbstrReprOfPage
  case object CacheTest_AbstrReprOfPage extends AbstrReprOfPage
}
