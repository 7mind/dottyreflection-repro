package test

import izumi.reflect.dottyreflection.Inspect.inspect

object Test2 extends App {

  // local class not ok
  locally {
    class Bar
    inspect[Bar]
  }

}
