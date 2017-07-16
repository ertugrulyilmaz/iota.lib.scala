package sota.dto.response

import scala.collection.mutable.ArrayBuffer

case class GetAttachToTangleResponse(duration: Long, trytes: ArrayBuffer[String]) extends AbstractResponse