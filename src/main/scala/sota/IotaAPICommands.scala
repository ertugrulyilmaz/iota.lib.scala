package sota

sealed trait IotaAPICommand {

  val command: String
  val params: Int

}

object IotaAPICommands {

  case object GET_NODE_INFO extends IotaAPICommand {
    override val command: String = "getNodeInfo"
    override val params: Int = 0
  }

  case object GET_NEIGHBORS extends IotaAPICommand {
    override val command: String = "getNeighbors"
    override val params: Int = 0
  }

  case object ADD_NEIGHBORS extends IotaAPICommand {
    override val command: String = "addNeighbors"
    override val params: Int = 0
  }

  case object REMOVE_NEIGHBORS extends IotaAPICommand {
    override val command: String = "removeNeighbors"
    override val params: Int = 0
  }

  case object GET_TIPS extends IotaAPICommand {
    override val command: String = "getTips"
    override val params: Int = 0
  }

  case object FIND_TRANSACTIONS extends IotaAPICommand {
    override val command: String = "findTransactions"
    override val params: Int = 0
  }

  case object GET_TRYTES extends IotaAPICommand {
    override val command: String = "getTrytes"
    override val params: Int = 0
  }

  case object GET_INCLUSIONS_STATES extends IotaAPICommand {
    override val command: String = "getInclusionStates"
    override val params: Int = 0
  }

  case object GET_BALANCES extends IotaAPICommand {
    override val command: String = "getBalances"
    override val params: Int = 0
  }

  case object GET_TRANSACTIONS_TO_APPROVE extends IotaAPICommand {
    override val command: String = "getTransactionsToApprove"
    override val params: Int = 0
  }

  case object ATTACH_TO_TANGLE extends IotaAPICommand {
    override val command: String = "attachToTangle"
    override val params: Int = 0
  }

  case object INTERRUPT_ATTACHING_TO_TANGLE extends IotaAPICommand {
    override val command: String = "interruptAttachingToTangle"
    override val params: Int = 0
  }

  case object BROADCAST_TRANSACTIONS extends IotaAPICommand {
    override val command: String = "broadcastTransactions"
    override val params: Int = 0
  }

  case object STORE_TRANSACTIONS extends IotaAPICommand {
    override val command: String = "storeTransactions"
    override val params: Int = 0
  }

}
