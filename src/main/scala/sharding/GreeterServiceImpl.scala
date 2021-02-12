package sharding

import akka.actor.typed.scaladsl.AskPattern.Askable
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.cluster.sharding.typed.ShardingEnvelope
import akka.cluster.sharding.typed.scaladsl.ClusterSharding
import akka.util.Timeout
import sharding.grpc._

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

class GreeterServiceImpl(sys: ActorSystem[_], sharding: ClusterSharding)
                        (implicit ec: ExecutionContext) extends GreeterService {

  implicit val timeout = Timeout(3 seconds)
  implicit val scheduler = sys.scheduler

  override def sayHello(req: HelloRequest): Future[HelloResponse] = {

    println(s"${Console.GREEN_B}RECEIVED REQUEST: ${req}${Console.RESET}")

    //Future.successful(HelloResponse(s"Hello, ${req.name}"))

    val ref = sharding.entityRefFor(Main.TypeKey, req.name)

    ref.ask { actor =>
      Greeter.Greet(req.name, actor)
    }
  }
}
