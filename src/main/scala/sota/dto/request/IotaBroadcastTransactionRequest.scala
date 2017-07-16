package sota.dto.request

import sota.IotaAPICommands.BROADCAST_TRANSACTIONS

case class IotaBroadcastTransactionRequest(override val command: String, trytes: Array[String]) extends CommandRequest

object IotaBroadcastTransactionRequest {

  def apply(trytes: String*): IotaBroadcastTransactionRequest = IotaBroadcastTransactionRequest(BROADCAST_TRANSACTIONS.command, Array(trytes:_*))

}


