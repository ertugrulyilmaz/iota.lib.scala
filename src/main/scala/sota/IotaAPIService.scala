package sota

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model.headers.`User-Agent`
import akka.http.scaladsl.model.{RequestEntity, _}
import akka.http.scaladsl.unmarshalling.{Unmarshal, Unmarshaller}
import akka.stream.{ActorMaterializer, Materializer}
import com.typesafe.scalalogging.StrictLogging
import sota.dto.request.{CommandRequest, IotaFindTransactionsRequest}
import sota.dto.response.{FindTransactionResponse, GetNodeInfoResponse}
import sota.pow.SCurl
import sota.protocol.{RequestSerialization, ResponseSerialization}

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future

trait IotaAPIService extends StrictLogging with ResponseSerialization with RequestSerialization {

  implicit val system = ActorSystem()
  implicit val materializer: Materializer = ActorMaterializer()
  implicit val dispatcher = system.dispatcher

  val headers = List(`User-Agent`("JOTA-API wrapper"))

  val protocol: String
  val host: String
  val port: Int
  protected val uri: String

  def getNodeInfo(data: CommandRequest): Future[GetNodeInfoResponse] = {
    request(data)
  }

  def findTransactions(findTransRequest: IotaFindTransactionsRequest): FindTransactionResponse = {
    FindTransactionResponse(1, ArrayBuffer.empty[String])
  }

  private def request[R >: CommandRequest, T](data: R): Future[T] = {
    val respEntity = for {
      request <- Marshal(data).to[RequestEntity]
      response <- Http().singleRequest(HttpRequest(method = POST, uri = uri, entity = request, headers = headers))
      entity <- deserialize[T](response)
    } yield entity

    respEntity
  }

  private def deserialize[T](response: HttpResponse)(implicit um: Unmarshaller[ResponseEntity, T]): Future[T] = {
    Unmarshal(response.entity).to[T]
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
