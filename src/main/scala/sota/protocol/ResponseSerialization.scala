package sota.protocol

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import sota.dto.response._
import spray.json._

trait ResponseSerialization extends SprayJsonSupport with DefaultJsonProtocol {

//  implicit val AddNeighborsResponseFormat = jsonFormat2(AddNeighborsResponse)
//
//  implicit val AnalyzeTransactionResponseFormat = jsonFormat2(AnalyzeTransactionResponse)
//
//  implicit val BroadcastTransactionsResponseFormat = jsonFormat1(BroadcastTransactionsResponse)
//
//  implicit val FindTransactionResponseFormat = jsonFormat2(FindTransactionResponse)
//
//  implicit val GetAttachToTangleResponseFormat = jsonFormat2(GetAttachToTangleResponse)
//
//  implicit val GetBalancesAndFormatResponseFormat = jsonFormat3(GetBalancesAndFormatResponse)
//
//  implicit val GetBalancesResponseFormat = jsonFormat4(GetBalancesResponse)
//
//  implicit val GetBundleResponseFormat = jsonFormat2(GetBundleResponse)
//
  implicit val GetNodeInfoResponseFormat = jsonFormat16(GetNodeInfoResponse)
//
//  implicit val GetNeighborsResponseFormat = jsonFormat2(GetNeighborsResponse)
//
//  implicit val GetNewAddressResponseFormat = jsonFormat2(GetNewAddressResponse)
//
//  implicit val GetTipsResponseFormat = jsonFormat2(GetTipsResponse)
//
//  implicit val GetTransactionsToApproveResponseFormat = jsonFormat3(GetTransactionsToApproveResponse)
//
//  implicit val GetTransferResponseFormat = jsonFormat2(GetTransferResponse)
//
//  implicit val GetTrytesResponseFormat = jsonFormat2(GetTrytesResponse)
//
//  implicit val InterruptAttachingToTangleResponseFormat = jsonFormat1(InterruptAttachingToTangleResponse)
//
//  implicit val RemoveNeighborsResponseFormat = jsonFormat2(RemoveNeighborsResponse)
//
//  implicit val ReplayBundleResponseFormat = jsonFormat2(ReplayBundleResponse)
//
//  implicit val SendTransferResponseFormat = jsonFormat2(SendTransferResponse)
//
//  implicit val StoreTransactionsResponseFormat = jsonFormat1(StoreTransactionsResponse)

}
