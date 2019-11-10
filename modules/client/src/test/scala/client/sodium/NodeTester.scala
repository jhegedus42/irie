package client.sodium

//import org.junit.Assert.assertTrue
//import org.junit.Test
import client.sodium.core.Node.Target
import org.scalatest.FunSuite
import client.sodium.core.Node
class NodeTester extends FunSuite {
  test("test node")  {
    val a = new Node(0)
    val b = new Node(1)
    val node_target_ = new Array[Target](1)
    a.linkTo(null, b, node_target_)
    assert(a.compareTo(b) < 0)
  }
}
