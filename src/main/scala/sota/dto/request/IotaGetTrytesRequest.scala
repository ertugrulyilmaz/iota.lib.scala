package sota.dto.request

import sota.IotaAPICommands.GET_TRYTES

case class IotaGetTrytesRequest(override val command: String, hashes: Array[String]) extends CommandRequest

object IotaGetTrytesRequest {

  def apply(hashes: String*): IotaGetTrytesRequest = IotaGetTrytesRequest(GET_TRYTES.command, Array(hashes: _*))

}
