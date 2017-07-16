package sota.protocol

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import sota.dto.request.IotaCommandRequest
import spray.json.DefaultJsonProtocol

trait RequestSerialization extends SprayJsonSupport with DefaultJsonProtocol {

  implicit val IotaCommandRequestFormat = jsonFormat1(IotaCommandRequest)

}
