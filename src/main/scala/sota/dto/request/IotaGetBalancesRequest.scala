package sota.dto.request

import sota.IotaAPICommands

case class IotaGetBalancesRequest(override val command: String, threshold: Int, addresses: Array[String])
  extends CommandRequest

object IotaGetBalancesRequest {

  def apply(threshold: Int, addresses: String*): IotaGetBalancesRequest =
    IotaGetBalancesRequest(IotaAPICommands.GET_BALANCES.command, threshold, Array(addresses: _*))

}