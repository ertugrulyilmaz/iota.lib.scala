package sota.utils

case class StopWatch(startTime: Long = System.currentTimeMillis, running: Boolean = true, currentTime: Long = 0) {

  def reStart(): StopWatch = {
    return copy(startTime = System.currentTimeMillis, running = true)
  }

  def stop(): StopWatch = {
    return copy(running = false)
  }

  def pause(): StopWatch = {
    return copy(startTime = System.currentTimeMillis - currentTime, running = true)
  }

  def getElapsedTimeMili(): Long = {
    if (running)
      return System.currentTimeMillis - startTime
    return 0
  }

  def getElapsedTimeSecs(): Long = {
    if (running)
      return (System.currentTimeMillis - startTime) / 1000
    return 0
  }

  def getElapsedTimeMins(): Long = {
    if (running)
      return (System.currentTimeMillis - startTime) / 1000 / 60
    return 0
  }

  def getElapsedTimeHours(): Long = {
    if (running)
      return (System.currentTimeMillis - startTime) / 1000 / 60 / 60
    return 0
  }

}

