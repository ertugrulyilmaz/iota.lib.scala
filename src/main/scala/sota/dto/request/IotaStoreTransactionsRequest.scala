package sota.dto.request

import sota.IotaAPICommands.STORE_TRANSACTIONS


case class IotaStoreTransactionsRequest(override val command: String, trytes: Array[String]) extends CommandRequest

object IotaStoreTransactionsRequest {

  def apply(trytes: String*): IotaStoreTransactionsRequest =
    IotaStoreTransactionsRequest(STORE_TRANSACTIONS.command, Array(trytes: _*))

}
