package sota.dto.response

case class GetInclusionStateResponse(duration: Long, states: Array[Boolean]) extends AbstractResponse
