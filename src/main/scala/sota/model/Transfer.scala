package sota.model

import spray.json._
import sota.protocol.TransferJsonProtocol._

case class Transfer(timestamp: String = "", var address: String, hash: String = "", persistence: Boolean = false, value: Long, message: String, tag: String) {

  override def toString: String = {
    return this.toJson.prettyPrint
  }

}
