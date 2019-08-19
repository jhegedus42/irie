package app.client.ui.caching.cacheInjector

import app.client.ui.caching.cacheInjector.injector.CacheInjectorHOC
import japgolly.scalajs.react.{CtorType, ScalaComponent}

case class ReactCompWrapper[Comp<:ToBeWrappedComponent[Comp]]
(
  cache: CacheInterface,
  propsProvider: () => Comp#Props,
  comp: ScalaComponent[
    CacheInterfaceWrapper[Comp#Props],
    Comp#State,
    Comp#Backend,
    CtorType.Props]
) {

  val wrapped_cachTestRootComp =
    new CacheInjectorHOC[Comp#Backend, Comp#Props, Comp#State](comp)

  val ciw: CacheInterfaceWrapper[Comp#Props] =
    CacheInterfaceWrapper[Comp#Props](
      cacheInterface = cache,
      props = propsProvider()
    )

  val wr = wrapped_cachTestRootComp.wrapperConstructor(ciw)

}
