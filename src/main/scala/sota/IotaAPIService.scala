package sota

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.{Marshal, Marshaller}
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model.headers.`User-Agent`
import akka.http.scaladsl.model.{RequestEntity, _}
import akka.http.scaladsl.unmarshalling.{Unmarshal, Unmarshaller}
import akka.stream.{ActorMaterializer, Materializer}
import com.typesafe.scalalogging.StrictLogging
import sota.dto.request._
import sota.dto.response._
import sota.protocol.{RequestSerialization, ResponseSerialization}

import scala.concurrent.Future

trait IotaAPIService extends StrictLogging with RequestSerialization with ResponseSerialization {

  implicit val system = ActorSystem()
  implicit val materializer: Materializer = ActorMaterializer()
  implicit val dispatcher = system.dispatcher

  val headers = List(`User-Agent`("JOTA-API wrapper"))

  val protocol: String
  val host: String
  val port: Int
  protected val uri: String

  private def request[R, T](data: R)(implicit m: Marshaller[R, RequestEntity], um: Unmarshaller[ResponseEntity, T]): Future[T] = {
    for {
      request <- Marshal(data).to[RequestEntity]
      response <- Http().singleRequest(HttpRequest(method = POST, uri = uri, entity = request, headers = headers))
      entity <- Unmarshal(response.entity).to[T]
    } yield entity
  }

  def getNodeInfo(data: IotaCommandRequest): Future[GetNodeInfoResponse] = {
    request[IotaCommandRequest, GetNodeInfoResponse](data)
  }

  def getNeighbors(data: IotaCommandRequest): Future[GetNeighborsResponse] = {
    request[IotaCommandRequest, GetNeighborsResponse](data)
  }

  def addNeighbors(data: IotaNeighborsRequest): Future[AddNeighborsResponse] = {
    request[IotaNeighborsRequest, AddNeighborsResponse](data)
  }

  def removeNeighbors(data: IotaNeighborsRequest): Future[RemoveNeighborsResponse] = {
    request[IotaNeighborsRequest, RemoveNeighborsResponse](data)
  }

  def getTips(data: IotaCommandRequest): Future[GetTipsResponse] = {
    request[IotaCommandRequest, GetTipsResponse](data)
  }

  def findTransactions(findTransRequest: IotaFindTransactionsRequest): FindTransactionResponse = {
    FindTransactionResponse(1, Array.empty[String])
  }

  def getInclusionStates(data: IotaGetInclusionStateRequest): Future[GetInclusionStateResponse] = {
    request[IotaGetInclusionStateRequest, GetInclusionStateResponse](data)
  }

  def getTrytes(data: IotaGetTrytesRequest): Future[GetTrytesResponse] = {
    request[IotaGetTrytesRequest, GetTrytesResponse](data)
  }

  def getTransactionsToApprove(data: IotaGetTransactionsToApproveRequest): Future[GetTransactionsToApproveResponse] = {
    request[IotaGetTransactionsToApproveRequest, GetTransactionsToApproveResponse](data)
  }

  def getBalances(data: IotaGetBalancesRequest): Future[GetBalancesResponse] = {
    request[IotaGetBalancesRequest, GetBalancesResponse](data)
  }

  def interruptAttachingToTangle(data: IotaCommandRequest): Future[InterruptAttachingToTangleResponse] = {
    request[IotaCommandRequest, InterruptAttachingToTangleResponse](data)
  }

  def attachToTangle(data: IotaAttachToTangleRequest): Future[GetAttachToTangleResponse] = {
    request[IotaAttachToTangleRequest, GetAttachToTangleResponse](data)
  }

  def storeTransactions(data: IotaStoreTransactionsRequest): Future[StoreTransactionsResponse] = {
    request[IotaStoreTransactionsRequest, StoreTransactionsResponse](data)
  }

  def broadcastTransactions(data: IotaBroadcastTransactionRequest): Future[BroadcastTransactionsResponse] = {
    request[IotaBroadcastTransactionRequest, BroadcastTransactionsResponse](data)
  }

}

object IotaAPIService {

  def apply(): IotaAPIService = new IotaAPIService {
    override val protocol: String = "http"
    override val host: String = "localhost"
    override val port: Int = 14265
    override val uri: String = s"$protocol://$host:$port/"
  }

  def apply(_protocol: String, _host: String, _port: Int): IotaAPIService = new IotaAPIService {
    override val protocol: String = _protocol
    override val host: String = _host
    override val port: Int = _port
    override val uri: String = s"$protocol://$host:$port/"
  }

}
