package app.shared.utils

import java.util.Calendar

import scala.language.experimental.macros
import scala.reflect.macros.whitebox.Context
import scala.annotation.{StaticAnnotation, compileTimeOnly}


@compileTimeOnly("enable macro paradise to expand macro annotations")
class MacroExampleMakeHelloWorld extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro ExampleMacro.impl
}
object ExampleMacro {
  def time=Calendar.getInstance.getTime
  def impl(c: Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._
    val result: List[Tree] = annottees.map(_.tree).toList match {
      case q"$mods val $tname: String = $expr" :: Nil =>
        val helloWorld = s" Compilation time was : $time."
        List(q"$mods val $tname: String = $expr + $helloWorld")
      case value @ q"$mods val $tname = $expr" :: Nil =>
        c.error(value.head.pos, s"$tname must have an explicit type")
        value
      case value @ q"$mods val $tname: $tpt = $expr" :: Nil =>
        c.error(value.head.pos, s"$tname must be of type String, not $tpt")
        value
      case tree =>
        c.error(tree.head.pos, "@Example can only be used with val of type String")
        tree
    }
    c.Expr[Any](Block(result, Literal(Constant(()))))
  }
}

