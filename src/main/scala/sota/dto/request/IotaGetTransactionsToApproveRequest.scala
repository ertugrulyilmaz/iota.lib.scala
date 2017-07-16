package sota.dto.request

import sota.IotaAPICommands.GET_TRANSACTIONS_TO_APPROVE

case class IotaGetTransactionsToApproveRequest(override val command: String, depth: Int) extends CommandRequest

object IotaGetTransactionsToApproveRequest {

  def apply(depth: Int): IotaGetTransactionsToApproveRequest =
    IotaGetTransactionsToApproveRequest(GET_TRANSACTIONS_TO_APPROVE.command, depth)

}
