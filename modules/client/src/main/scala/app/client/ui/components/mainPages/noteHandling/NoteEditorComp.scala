package app.client.ui.components.mainPages.noteHandling

import app.client.ui.caching.cacheInjector.ToBeWrappedMainPageComponent
import app.client.ui.components.MainPageInjectedWithCacheAndController
import app.client.ui.components.mainPages.noteHandling.NoteEditorComp.NoteEditorPage

trait NoteEditorComp
    extends ToBeWrappedMainPageComponent[NoteEditorComp,
                                         NoteEditorPage] {}

object NoteEditorComp {

  case class NoteEditorPage(uuidFromURL: String)
      extends MainPageInjectedWithCacheAndController[NoteEditorComp,
                                                     NoteEditorPage]

  // todo-now CONTINUE HERE
  //  step 1
  //  print uuidFromURL
  //  step 2
  //  navigate to this page from the note list comp using a link
  //  step 3
  //  print details for the note whose UUID is received from the NoteEditorPage


}
