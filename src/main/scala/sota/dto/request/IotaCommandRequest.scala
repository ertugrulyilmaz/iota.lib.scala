package sota.dto.request

case class IotaCommandRequest(override val command: String) extends CommandRequest

case class IotaAttachToTangleRequest(override val command: String, trunkTransaction: String, branchTransaction: String,
                                     minWeightMagnitude: Int, trytes: Array[String]) extends CommandRequest

case class IotaBroadcastTransactionRequest(override val command: String, trytes: Array[String]) extends CommandRequest

case class IotaGetBalancesRequest(override val command: String, threshold: Int, addresses: Array[String]) extends CommandRequest

case class IotaGetInclusionStateRequest(override val command: String, transactions: Array[String], tips: Array[String]) extends CommandRequest

case class IotaGetTransactionsToApproveRequest(override val command: String, depth: Int) extends CommandRequest

case class IotaGetTrytesRequest(override val command: String, hashes: Array[String]) extends CommandRequest

case class IotaNeighborsRequest(override val command: String, uris: Array[String]) extends CommandRequest

case class IotaStoreTransactionsRequest(override val command: String, trytes: Array[String]) extends CommandRequest

case class IotaFindTransactionsRequest(override val command: String, bundles: Array[String], addresses: Array[String], tags: Array[String], approvees: Array[String]) extends CommandRequest
