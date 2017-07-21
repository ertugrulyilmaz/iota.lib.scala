package sota.protocol

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import sota.dto.request._
import spray.json.DefaultJsonProtocol

trait RequestSerialization extends SprayJsonSupport with DefaultJsonProtocol {

  implicit val IotaCommandRequestFormat = jsonFormat1(IotaCommandRequest)

  implicit val IotaAttachToTangleRequestFormat = jsonFormat5(IotaAttachToTangleRequest)

  implicit val IotaBroadcastTransactionRequestFormat = jsonFormat2(IotaBroadcastTransactionRequest)

  implicit val IotaGetBalancesRequestFormat = jsonFormat3(IotaGetBalancesRequest)

  implicit val IotaGetInclusionStateRequestFormat = jsonFormat3(IotaGetInclusionStateRequest)

  implicit val IotaGetTransactionsToApproveRequestFormat = jsonFormat2(IotaGetTransactionsToApproveRequest)

  implicit val IotaGetTrytesRequestFormat = jsonFormat2(IotaGetTrytesRequest)

  implicit val IotaNeighborsRequestFormat = jsonFormat2(IotaNeighborsRequest)

  implicit val IotaStoreTransactionsRequestFormat = jsonFormat2(IotaStoreTransactionsRequest)

}
