package sota.dto.response

case class GetNodeInfoResponse(duration: Long, appName: String, appVersion: String, jreAvailableProcessors: Int,
                               jreFreeMemory: Long, jreMaxMemory: Long, jreTotalMemory: Long, latestMilestone: String,
                               latestMilestoneIndex: Int, latestSolidSubtangleMilestone: String,
                               latestSolidSubtangleMilestoneIndex: Int, neighbors: Int, packetsQueueSize: Int,
                               time: Long, tips: Int, transactionsToRequest: Int) extends AbstractResponse
