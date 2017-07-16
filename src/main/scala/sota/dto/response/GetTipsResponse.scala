package sota.dto.response

import scala.collection.mutable.ArrayBuffer

case class GetTipsResponse(duration: Long, hashes: ArrayBuffer[String]) extends AbstractResponse