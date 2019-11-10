package client.sodium.core

trait TransactionHandler[A] {
  def run(trans: Transaction, a: A): Unit
}
