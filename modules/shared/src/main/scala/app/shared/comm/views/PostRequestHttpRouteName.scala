package app.shared.comm.views

import scala.reflect.ClassTag


case class PostRequestHttpRouteName(name:String){
  def getPathNameForClient:String= "/"+ name
}



object PostRequestHttpRouteName{

  // Random UUID: bfaec55006204e778925b4b2b911319b
  // commit 3a7d0bc1c81a6f3d8e6aa3b6d286e8e0291af5d5
  // Date: Sun Sep  2 19:25:15 EEST 2018
  def getPostRequestHttpRouteName[V<:PostRequest:ClassTag]:
  PostRequestHttpRouteName={
    val viewName=ViewName.getViewName[V]
    PostRequestHttpRouteName("getView_" + viewName.simpleClassName)
  }
}



