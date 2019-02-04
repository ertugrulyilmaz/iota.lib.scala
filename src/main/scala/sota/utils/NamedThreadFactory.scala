package sota.utils

import java.util.concurrent.{Executors, ThreadFactory}
import java.util.concurrent.atomic.AtomicInteger

class NamedThreadFactory(baseName: String) extends ThreadFactory {

  private val threadNum: AtomicInteger = new AtomicInteger(0)

  override def newThread(r: Runnable): Thread = {
    val t: Thread = Executors.defaultThreadFactory().newThread(r)

    t.setName(baseName + "-" + threadNum.getAndIncrement().toString)

    t
  }

}
