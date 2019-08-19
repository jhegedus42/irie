package app.shared.comm.requests

import scala.reflect.ClassTag

case class PostRequestName(simpleClassName:String)

object PostRequestName
{
  def getPostRequestName[V<:Request:ClassTag] : PostRequestName = {
    val name=implicitly[ClassTag[V]].runtimeClass.getSimpleName
    PostRequestName(simpleClassName = name)
  }
}