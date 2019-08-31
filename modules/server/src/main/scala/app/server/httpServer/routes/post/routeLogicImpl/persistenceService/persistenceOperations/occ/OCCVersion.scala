package app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.occ

case class OCCVersion(version:Long){

  def inc:OCCVersion={
    val newVersion: Long =version+1
    OCCVersion(newVersion)
  }
  // todo-later : test OCC
  //  bank transaction:
  //   two transactions:
  //   1 ) from Alice to Bob
  //   2 ) and from Alice to Cica
  //   there is not enough money on Alice's account for both transactions
  //   OCC should prevent Alice's account to be overdrawn and reject one of the
  //   two transactions
  //   - for testing purposes, we should create a TestOCCBankTransaction "route"
  //     which contains from whom to whom, how much should be transferred, and also,
  //     a time, which tells the server how long the "background check"/"processings or
  //     executing the transaction should take" => this should result in a predictable
  //     OCC "locking" / "rejection" behaviour, which can be unit tested
}

