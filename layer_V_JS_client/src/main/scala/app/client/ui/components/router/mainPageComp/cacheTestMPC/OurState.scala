package app.client.ui.components.router.mainPageComp.cacheTestMPC

import app.shared.rest.views.viewsForDevelopingTheViewFramework.SumIntView_HolderObject.SumIntView_Par
import monocle.macros.Lenses

@Lenses
case class OurState(tn: TheThieveryNumber, sumIntViewPars: SumIntView_Par )
