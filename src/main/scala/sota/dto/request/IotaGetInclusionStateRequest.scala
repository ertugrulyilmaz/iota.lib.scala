package sota.dto.request

import sota.IotaAPICommands.GET_INCLUSIONS_STATES

case class IotaGetInclusionStateRequest(override val command: String, transactions: Array[String], tips: Array[String])
  extends CommandRequest

object IotaGetInclusionStateRequest {

  def apply(transactions: Array[String], tips: Array[String]): IotaGetInclusionStateRequest =
    IotaGetInclusionStateRequest(GET_INCLUSIONS_STATES.command, transactions, tips)

}
