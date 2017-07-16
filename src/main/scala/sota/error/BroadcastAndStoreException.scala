package sota.error

/**
  * This exceptions occurs if its not possible to broadcast and store.
  */
class BroadcastAndStoreException private(msg: String) extends BaseException(msg) {

  def this() {
    this("Impossible to broadcastAndStore, aborting.")
  }

}
