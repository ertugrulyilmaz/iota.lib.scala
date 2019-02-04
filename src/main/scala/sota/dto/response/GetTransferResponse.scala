package sota.dto.response

import sota.model.Bundle

case class GetTransferResponse(duration: Long, transferBundle: Array[Bundle]) extends AbstractResponse