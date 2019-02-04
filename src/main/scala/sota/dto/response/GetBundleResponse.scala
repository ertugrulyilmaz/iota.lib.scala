package sota.dto.response

import sota.model.Transaction

case class GetBundleResponse(duration: Long, transactions: List[Transaction]) extends AbstractResponse