package app.shared.rest

/**
  * Created by joco on 07/12/2017.
  */
object URLs {

}


case class URLServerSide(urlServerSide:String) {
  def urlClientSide="/"+urlServerSide
}
