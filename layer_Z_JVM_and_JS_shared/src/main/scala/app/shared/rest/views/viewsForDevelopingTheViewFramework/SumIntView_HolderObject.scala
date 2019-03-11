package app.shared.rest.views.viewsForDevelopingTheViewFramework

import app.copy_of_model_to_be_moved_to_real_app.getViewCommunicationModel.shared.views.{Parameter, Result, View}


// 9d8879cd0e3e44e6b28d75f7e6f28662

object SumIntView_HolderObject {

    // nemm kell parazni,az osszes View nev az itt van definialva

    // corresponds to a1704dea5c4b41bf8297f4d7a9f3c3af
    case class SumIntView_Par(x:Int, y:Int ) extends Parameter

    case class SumIntView_Res(sum: Int ) extends Result
    // 8c42c673e5f34b4481db743cbd255152$f613bee1c9520139dfa883a5b364d39c2d2ed17c


  class SumIntView extends View {
      type Par = SumIntView_Par
      type Res = SumIntView_Res
    }

}
