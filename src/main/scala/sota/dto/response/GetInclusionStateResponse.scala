package sota.dto.response

import scala.collection.mutable.ArrayBuffer

case class GetInclusionStateResponse(duration: Long, states: ArrayBuffer[Boolean]) extends AbstractResponse