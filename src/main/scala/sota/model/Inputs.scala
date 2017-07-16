package sota.model
import sota.protocol.InputJsonProtocol._
import spray.json._

case class Inputs(inputsList: Seq[Input], totalBalance: Long) {

  override def toString: String = {

    return this.toJson.toString
  }

}
