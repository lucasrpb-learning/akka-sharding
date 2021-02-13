package sharding

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.grpc.GrpcClientSettings
import com.typesafe.config.ConfigFactory
import sharding.grpc.{GreeterServiceClient, HelloRequest, HelloResponse}

import java.util.concurrent.ThreadLocalRandom
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._

object Client {

  def main(args: Array[String]): Unit = {

    implicit val sys = ActorSystem(Behaviors.empty[Any], "Hello", ConfigFactory.load("client.conf"))
    implicit val ec: ExecutionContext = sys.executionContext

    /*val client = GreeterServiceClient(GrpcClientSettings.fromConfig("greeter.GreeterService")
      .withTls(false))*/

    val rand = ThreadLocalRandom.current()

    val ports = Seq(3551, 3552)

    val port = ports(rand.nextInt(0, ports.length))
    val client = GreeterServiceClient(GrpcClientSettings.connectToServiceAt("localhost", port)
      .withTls(false))

    val names = Seq("Monica", "Lucas", "Luana", "2", "2")

    def greet(name: String): Future[HelloResponse] = {
      client.sayHello(HelloRequest(name))
    }

    val f = Future.sequence(names.map{greet(_)}).map { results =>
      sys.terminate()
      results
    }

    val response = Await.result(f, Duration.Inf)

    println(s"${Console.GREEN_B}RECEIVED RESPONSE ${response}${Console.RESET}")

  }

}
