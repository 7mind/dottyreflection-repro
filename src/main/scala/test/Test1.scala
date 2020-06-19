package test

import izumi.reflect.dottyreflection.Inspect.inspect

object Test1 extends App {

  inspect[scala.collection.immutable.List[Int]] // ok
  inspect[java.lang.String] // ok
  inspect[String] // ok
  inspect[List[Unit]] // ok
  inspect[Tuple1[Unit]] // Tuple1-22 - not ok
  //Error:(11, 10) Exception occurred while executing macro expansion.
  //java.lang.AssertionError: assertion failed
  //	at dotty.DottyPredef$.assertFail(DottyPredef.scala:16)
  //	at dotty.tools.dotc.ast.tpd$.polyDefDef(tpd.scala:239)
  //	at dotty.tools.dotc.ast.tpd$.DefDef(tpd.scala:225)
  //	at dotty.tools.dotc.ast.tpd$.DefDef(tpd.scala:222)
  //	at dotty.tools.dotc.tastyreflect.FromSymbol$.classDef(FromSymbol.scala:33)
  //	at dotty.tools.dotc.tastyreflect.FromSymbol$.definitionFromSym(FromSymbol.scala:16)
  //	at dotty.tools.dotc.tastyreflect.ReflectionCompilerInterface.Symbol_tree(ReflectionCompilerInterface.scala:1681)
  //	at dotty.tools.dotc.tastyreflect.ReflectionCompilerInterface.Symbol_tree(ReflectionCompilerInterface.scala:1680)
  //	at scala.tasty.Reflection$SymbolOps$.tree(Reflection.scala:2194)
  //	at izumi.reflect.dottyreflection.Inspect$.inspectTpe(Inspect.scala:9)

}
