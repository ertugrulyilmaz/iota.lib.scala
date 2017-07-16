package sota.protocol

import sota.model.{Input, Inputs}
import spray.json._

object InputJsonProtocol extends DefaultJsonProtocol {

  implicit val inputFormat = jsonFormat4(Input)

  implicit val inputsFormat = jsonFormat2(Inputs)

}