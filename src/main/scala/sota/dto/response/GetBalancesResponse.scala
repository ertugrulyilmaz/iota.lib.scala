package sota.dto.response

import scala.collection.mutable.ArrayBuffer

case class GetBalancesResponse(duration: Long, balances: ArrayBuffer[String], milestone: String, milestoneIndex: Int) extends AbstractResponse