package app.shared.utils.macros

import scala.reflect.ClassTag

object GetSimpleClassName {

  def simpleName[C: ClassTag]: String =
    implicitly[ClassTag[C]].runtimeClass.getSimpleName

}
