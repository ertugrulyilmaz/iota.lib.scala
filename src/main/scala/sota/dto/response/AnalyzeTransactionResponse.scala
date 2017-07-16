package sota.dto.response

import sota.model.Transaction

case class AnalyzeTransactionResponse(duration: Long, transactions: List[Transaction]) extends AbstractResponse