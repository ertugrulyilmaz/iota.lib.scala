import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.StrictLogging
import sota.IotaAPI
import sota.protocol.ResponseSerialization

object App extends App with StrictLogging with ResponseSerialization {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val dispatcher = system.dispatcher

  val api: IotaAPI = IotaAPI()
  val r = api.getNodeInfo().foreach(println)

}
