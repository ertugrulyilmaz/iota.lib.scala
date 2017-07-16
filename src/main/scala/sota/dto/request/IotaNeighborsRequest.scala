package sota.dto.request

import sota.IotaAPICommands.{ADD_NEIGHBORS, REMOVE_NEIGHBORS}

case class IotaNeighborsRequest(override val command: String, uris: Array[String]) extends CommandRequest

object IotaNeighborsRequest {

  def createAddNeighborsRequest(uris: String*): IotaNeighborsRequest =
    IotaNeighborsRequest(ADD_NEIGHBORS.command, Array(uris: _*))

  def createRemoveNeighborsRequest(uris: String*): IotaNeighborsRequest =
    IotaNeighborsRequest(REMOVE_NEIGHBORS.command, Array(uris: _*))

}
