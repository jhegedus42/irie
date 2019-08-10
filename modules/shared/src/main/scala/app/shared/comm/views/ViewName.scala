package app.shared.comm.views

import scala.reflect.ClassTag

case class ViewName(simpleClassName:String)

object ViewName
{
  def getViewName[V<:View:ClassTag] : ViewName = {
    val name=implicitly[ClassTag[V]].runtimeClass.getSimpleName
    ViewName(simpleClassName = name)
  }
}