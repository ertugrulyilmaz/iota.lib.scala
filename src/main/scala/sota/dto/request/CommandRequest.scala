package sota.dto.request

import sota.{IotaAPICommand, IotaAPICommands}

trait CommandRequest {
  val command: String
}

object CommandRequest {

  def apply(apiCommand: IotaAPICommand): IotaCommandRequest = IotaCommandRequest(apiCommand.command)

  def createNodeInfoRequest(): IotaCommandRequest = apply(IotaAPICommands.GET_NODE_INFO)

  def createGetTipsRequest(): IotaCommandRequest = apply(IotaAPICommands.GET_TIPS)

  def createGetNeighborsRequest(): IotaCommandRequest = apply(IotaAPICommands.GET_NEIGHBORS)

  def createInterruptAttachToTangleRequest(): IotaCommandRequest = apply(IotaAPICommands.INTERRUPT_ATTACHING_TO_TANGLE)

}
