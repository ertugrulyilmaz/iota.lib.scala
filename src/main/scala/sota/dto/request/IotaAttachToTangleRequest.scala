package sota.dto.request

import sota.IotaAPICommands.ATTACH_TO_TANGLE

case class IotaAttachToTangleRequest(override val command: String, trunkTransaction: String, branchTransaction: String,
                                     minWeightMagnitude: Int, trytes: Array[String]) extends CommandRequest

object IotaAttachToTangleRequest {

  def apply(trunkTransaction: String, branchTransaction: String, minWeightMagnitude: Int, trytes: String*): IotaAttachToTangleRequest =
    new IotaAttachToTangleRequest(ATTACH_TO_TANGLE.command, trunkTransaction, branchTransaction, minWeightMagnitude, Array(trytes: _*))

}
