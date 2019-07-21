package app.shared.utils.logging

object LoggingHelperFunctions {
  def logMethodCall(methodName:String, whatHappened:String):Unit={
    println(
      s"""
        |
        |
        |
        | ---------------vvvvvvvvvvvvvvvvvvvvvvvvvv-------------------------
        | STORY BEGINS HERE
        |
        |
        | method `$methodName` was called
        |
        | and the following happened due to this call :
        |
        |
        | $whatHappened
        |
        |
        |STORY ENDS HERE
        |-----------------^^^^^^^^^^^^^^^^^^^^^^^^--------------------------
        |
        |
        |
       """.stripMargin
    )

  }

}
