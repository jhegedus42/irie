package app.shared.comm.views

import scala.reflect.ClassTag

object ViewHttpRouteNameProvider{

  // Random UUID: bfaec55006204e778925b4b2b911319b
  // commit 3a7d0bc1c81a6f3d8e6aa3b6d286e8e0291af5d5
  // Date: Sun Sep  2 19:25:15 EEST 2018
  def getViewHttpRouteName[V<:View:ClassTag]:
    ViewHttpRouteName={
      val viewName=ViewName.getViewName[V]
    ViewHttpRouteName("getView_" + viewName.simpleClassName)
  }
}
