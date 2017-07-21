package sota.protocol

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import sota.dto.response._
import sota.model.{Bundle, Input, Neighbor, Transaction}
import spray.json._

trait ResponseSerialization extends SprayJsonSupport with DefaultJsonProtocol {

  implicit object TransactionFormat extends RootJsonFormat[Transaction] {
    def write(t: Transaction) =
      JsArray(JsString(t.address), JsNumber(t.value), JsString(t.tag), JsNumber(t.timestamp))

    def read(jsValue: JsValue) = jsValue match {
      case JsArray(Vector(JsString(address), JsNumber(value), JsString(tag), JsNumber(timestamp))) =>
        Transaction(address, value.toLong, tag, timestamp.toLong)
      case _ => deserializationError("Transaction expected")
    }
  }

  implicit val BundleFormat = jsonFormat0(Bundle)

  implicit val InputFormat = jsonFormat4(Input)

  implicit val NeighborFormat = jsonFormat4(Neighbor)

  implicit val AddNeighborsResponseFormat = jsonFormat2(AddNeighborsResponse)

  implicit val AnalyzeTransactionResponseFormat = jsonFormat2(AnalyzeTransactionResponse)

  implicit val BroadcastTransactionsResponseFormat = jsonFormat1(BroadcastTransactionsResponse)

  implicit val FindTransactionResponseFormat = jsonFormat2(FindTransactionResponse)

  implicit val GetAttachToTangleResponseFormat = jsonFormat2(GetAttachToTangleResponse)

  implicit val GetBalancesAndFormatResponseFormat = jsonFormat3(GetBalancesAndFormatResponse)

  implicit val GetBalancesResponseFormat = jsonFormat4(GetBalancesResponse)

  implicit val GetBundleResponseFormat = jsonFormat2(GetBundleResponse)

  implicit val GetNodeInfoResponseFormat = jsonFormat16(GetNodeInfoResponse)

  implicit val GetNeighborsResponseFormat = jsonFormat2(GetNeighborsResponse)

  implicit val GetNewAddressResponseFormat = jsonFormat2(GetNewAddressResponse)

  implicit val GetTipsResponseFormat = jsonFormat2(GetTipsResponse)

  implicit val GetTransactionsToApproveResponseFormat = jsonFormat3(GetTransactionsToApproveResponse)

  implicit val GetTransferResponseFormat = jsonFormat2(GetTransferResponse)

  implicit val GetTrytesResponseFormat = jsonFormat2(GetTrytesResponse)

  implicit val InterruptAttachingToTangleResponseFormat = jsonFormat1(InterruptAttachingToTangleResponse)

  implicit val RemoveNeighborsResponseFormat = jsonFormat2(RemoveNeighborsResponse)

  implicit val ReplayBundleResponseFormat = jsonFormat2(ReplayBundleResponse)

  implicit val SendTransferResponseFormat = jsonFormat2(SendTransferResponse)

  implicit val StoreTransactionsResponseFormat = jsonFormat1(StoreTransactionsResponse)

  implicit val GetInclusionStateResponseFormat = jsonFormat2(GetInclusionStateResponse)

}
