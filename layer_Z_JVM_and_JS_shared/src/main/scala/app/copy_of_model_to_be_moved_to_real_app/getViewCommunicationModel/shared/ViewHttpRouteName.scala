package app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.shared

import app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.shared.views.View

import scala.reflect.ClassTag


case class ViewHttpRouteName(name:String){
  def getPathNameForClient:String= "/"+ name
  def getPathNameForAkkaHttpRoutePathDirective: String = name
}

object ViewHttpRouteNameProvider{

  // Random UUID: bfaec55006204e778925b4b2b911319b
  // commit 3a7d0bc1c81a6f3d8e6aa3b6d286e8e0291af5d5
  // Date: Sun Sep  2 19:25:15 EEST 2018
  def getViewHttpRouteName[V<:View:ClassTag]():
    ViewHttpRouteName={
      val viewName=ViewName.getViewName[V]()
    ViewHttpRouteName("getView_" + viewName.simpleClassName)
  }
}

case class ViewName(simpleClassName:String)

object ViewName
{
  def getViewName[V<:View:ClassTag]() : ViewName = {
//    val full=implicitly[ClassTag[V]].runtimeClass.getCanonicalName
//    val short=implicitly[ClassTag[V]].runtimeClass.getSimpleName
//    val name=implicitly[ClassTag[V]].runtimeClass.getName
    val name=implicitly[ClassTag[V]].runtimeClass.getSimpleName
    ViewName(simpleClassName = name)
  }
}
