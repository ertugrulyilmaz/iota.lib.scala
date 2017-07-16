package sota

import sota.dto.request.{CommandRequest, IotaFindTransactionsRequest}
import sota.dto.response._
import sota.pow.SCurl

import scala.concurrent.Future

trait IotaAPICore {

  val customCurl: SCurl
  val service: IotaAPIService

  def getNodeInfo(): Future[GetNodeInfoResponse] = {
    service.getNodeInfo(CommandRequest.createNodeInfoRequest())
  }

  def getNeighbors(): Future[GetNeighborsResponse] = ???

  def addNeighbors(uris: String*): AddNeighborsResponse = ???

  def removeNeighbors(uris: String*): RemoveNeighborsResponse = ???

  def getTips(): Option[GetTipsResponse] = ???

  def findTransactions(addresses: Array[String], tags: Array[String], approvees: Array[String], bundles: Array[String]): Option[FindTransactionResponse] = {
    val findTransRequest = IotaFindTransactionsRequest()
      .byAddresses(addresses: _*)
      .byTags(tags: _*)
      .byApprovees(approvees: _*)
      .byBundles(bundles: _*)


    Some(service.findTransactions(findTransRequest))
  }

  def findTransactionsByAddresses(addresses: String*): Option[FindTransactionResponse] =
    findTransactions(Array(addresses: _*), null, null, null)

  def findTransactionsByDigests(digests: String*): Option[FindTransactionResponse] =
    findTransactions(null, Array(digests: _*), null, null)

  def findTransactionsByApprovees(approvees: String*): Option[FindTransactionResponse] =
    findTransactions(null, null, Array(approvees: _*), null)

  def findTransactionsByBundles(bundles: String*): Option[FindTransactionResponse] =
    findTransactions(null, null, null, Array(bundles: _*))

  def getInclusionStates(transactions: Array[String], tips: Array[String]): GetInclusionStateResponse = ???

  def getTrytes(hashes: String*): Option[GetTrytesResponse] = ???

  def getTransactionsToApprove(depth: Int): GetTransactionsToApproveResponse = ???

  def getBalances(threshold: Int, addresses: Array[String]): GetBalancesResponse = ???

  def getBalances(threshold: Int, addresses: List[String]): GetBalancesResponse = ???

  def interruptAttachingToTangle(): InterruptAttachingToTangleResponse = ???

  def attachToTangle(trunkTransaction: String, branchTransaction: String, minWeightMagnitude: Int, trytes: String*): GetAttachToTangleResponse = ???

  def storeTransactions(trytes: String*): StoreTransactionsResponse = ???

  def broadcastTransactions(trytes: String*): BroadcastTransactionsResponse = ???



}
