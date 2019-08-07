package app.testHelpersShared.data

import app.shared.data.model.Note
import app.shared.data.ref.{TypedRefVal, RefValDyn, TypedRef, Version}

/**
  * Created by joco on 09/10/2017.
  */
object TestEntities {
  val theUUIDofTheLine           = "4ce6fca0-0fd5-4197-a946-90f5e7e00d9d"
  val theUUIDofTheLine_incorrect = "4ce6fca0-0fd5-4197-a946-90f5e7e00d9R"
  val line                       = Note( content = "text" )
  val refToLine: TypedRef[Note] = TypedRef.makeWithUUID( theUUIDofTheLine )

  val refValOfLineV0 = TypedRefVal( refToLine, line, Version() )

  val refValOfLineV1 = TypedRefVal( refToLine, line, Version().inc() )

}
