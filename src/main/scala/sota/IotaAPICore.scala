package sota

import sota.IotaAPICommands._
import sota.dto.request._
import sota.dto.response._
import sota.pow.SCurl

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

trait IotaAPICore {

  val customCurl: SCurl
  val service: IotaAPIService

  def getNodeInfo(): Future[GetNodeInfoResponse] = service.getNodeInfo(CommandRequest.createNodeInfoRequest())

  def getNeighbors(): Future[GetNeighborsResponse] = service.getNeighbors(CommandRequest.createGetNeighborsRequest())

  def addNeighbors(uris: String*): Future[AddNeighborsResponse] = service.addNeighbors(IotaNeighborsRequest(ADD_NEIGHBORS.command, Array(uris: _*)))

  def removeNeighbors(uris: String*): Future[RemoveNeighborsResponse] = service.removeNeighbors(IotaNeighborsRequest(REMOVE_NEIGHBORS.command, Array(uris: _*)))

  def getTips(): Future[GetTipsResponse] = service.getTips(CommandRequest.createGetTipsRequest())

  def findTransactions(addresses: Array[String], tags: Array[String], approvees: Array[String], bundles: Array[String]): Option[FindTransactionResponse] =
    Some(service.findTransactions(IotaFindTransactionsRequest(FIND_TRANSACTIONS.command, bundles, addresses, tags, approvees)))

  def findTransactionsByAddresses(addresses: String*): Option[FindTransactionResponse] =
    findTransactions(Array(addresses: _*), null, null, null)

  def findTransactionsByDigests(digests: String*): Option[FindTransactionResponse] =
    findTransactions(null, Array(digests: _*), null, null)

  def findTransactionsByApprovees(approvees: String*): Option[FindTransactionResponse] =
    findTransactions(null, null, Array(approvees: _*), null)

  def findTransactionsByBundles(bundles: String*): Option[FindTransactionResponse] =
    findTransactions(null, null, null, Array(bundles: _*))

  def getInclusionStates(transactions: Array[String], tips: Array[String]): GetInclusionStateResponse =
    Await.result(service.getInclusionStates(IotaGetInclusionStateRequest(GET_INCLUSIONS_STATES.command, transactions, tips)), Duration.Inf)

  def getTrytes(hashes: String*): Option[GetTrytesResponse] =
    Some(Await.result(service.getTrytes(IotaGetTrytesRequest(GET_TRYTES.command, Array(hashes: _*))), Duration.Inf))

  def getTransactionsToApprove(depth: Int): GetTransactionsToApproveResponse =
    Await.result(service.getTransactionsToApprove(IotaGetTransactionsToApproveRequest(GET_TRANSACTIONS_TO_APPROVE.command, depth)), Duration.Inf)

  def getBalances(threshold: Int, addresses: Array[String]): GetBalancesResponse =
    Await.result(service.getBalances(IotaGetBalancesRequest(GET_BALANCES.command, threshold, addresses)), Duration.Inf)

  def getBalances(threshold: Int, addresses: List[String]): GetBalancesResponse =
    Await.result(service.getBalances(IotaGetBalancesRequest(GET_BALANCES.command, threshold, addresses.toArray)), Duration.Inf)

  def interruptAttachingToTangle(): Future[InterruptAttachingToTangleResponse] = service.interruptAttachingToTangle(CommandRequest.createInterruptAttachToTangleRequest())

  def attachToTangle(trunkTransaction: String, branchTransaction: String, minWeightMagnitude: Int, trytes: String*): GetAttachToTangleResponse =
    Await.result(service.attachToTangle(IotaAttachToTangleRequest(ATTACH_TO_TANGLE.command, trunkTransaction, branchTransaction, minWeightMagnitude, Array(trytes: _*))), Duration.Inf)

  def storeTransactions(trytes: String*): StoreTransactionsResponse =
    Await.result(service.storeTransactions(IotaStoreTransactionsRequest(STORE_TRANSACTIONS.command, Array(trytes: _*))), Duration.Inf)

  def broadcastTransactions(trytes: String*): Future[BroadcastTransactionsResponse] = service.broadcastTransactions(IotaBroadcastTransactionRequest(BROADCAST_TRANSACTIONS.command, Array(trytes: _*)))

}
