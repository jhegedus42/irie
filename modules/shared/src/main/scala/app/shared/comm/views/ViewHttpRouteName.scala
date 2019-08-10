package app.shared.comm.views

import scala.reflect.ClassTag


case class ViewHttpRouteName(name:String){
  def getPathNameForClient:String= "/"+ name
  def getPathNameForAkkaHttpRoutePathDirective: String = name
}






