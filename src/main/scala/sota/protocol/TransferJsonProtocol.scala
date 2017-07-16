package sota.protocol

import sota.model.Transfer
import spray.json.DefaultJsonProtocol

object TransferJsonProtocol extends DefaultJsonProtocol {

  implicit val transferFormat = jsonFormat7(Transfer)

}
