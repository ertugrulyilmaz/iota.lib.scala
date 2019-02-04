package sota.dto.response

case class GetAttachToTangleResponse(duration: Long, trytes: Seq[String]) extends AbstractResponse
