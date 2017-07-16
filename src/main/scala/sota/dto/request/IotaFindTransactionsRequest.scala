package sota.dto.request

import sota.IotaAPICommands

sealed trait IotaFindTransactionsRequest extends CommandRequest {

  var bundles: Array[String] = Array.empty[String]
  var addresses: Array[String] = Array.empty[String]
  var tags: Array[String] = Array.empty[String]
  var approvees: Array[String] = Array.empty[String]

  def byBundles(_bundles: String*): IotaFindTransactionsRequest = {
    bundles = Array(_bundles: _*)

    this
  }

  def byAddresses(_addresses: String*): IotaFindTransactionsRequest = {
    addresses = Array(_addresses: _*)

    this
  }

  def byTags(_tags: String*): IotaFindTransactionsRequest = {
    tags = Array(_tags: _*)

    this
  }

  def byApprovees(_approvees: String*): IotaFindTransactionsRequest = {
    approvees = Array(_approvees: _*)

    this
  }

}

object IotaFindTransactionsRequest {

  def apply(): IotaFindTransactionsRequest = new IotaFindTransactionsRequest {
    override val command: String = IotaAPICommands.FIND_TRANSACTIONS.command
  }

}
