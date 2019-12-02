package client.sodium.app

import client.sodium.core.{
  CellLoop,
  Stream,
  StreamSink,
  Transaction
}

case class CellLoopWithUpdaterStream[V](initValue: V) {

  val updaterStream: StreamSink[V => V] =
    new StreamSink[V => V]()

  val snapshotter: StreamSink[Unit] = new StreamSink[Unit]()

//  def merge(c: Cell[V]):

//  val setter:StreamSink[V]=new StreamSink[V]()

  val cellLoop = Transaction.apply[CellLoop[V]](
    { _ =>
      lazy val afterUpdate: Stream[V] =
        updaterStream.snapshot(
          counterValue, { (f: V => V, c: V) =>
            f(c)
          }
        )

      lazy val counterValue: CellLoop[V] = new CellLoop[V]()

      counterValue.loop(afterUpdate.hold(initValue))

      counterValue
    }
  )

}
