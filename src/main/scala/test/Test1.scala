package test

import izumi.reflect.dottyreflection.Inspect.inspect

object Test1 extends App {

  // ok in class body
  class Foo
  println(inspect[Foo])

  // not ok in expression
  def test() = {
    locally {
      inspect[Foo]
    }
  }

}