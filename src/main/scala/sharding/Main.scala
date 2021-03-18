package sharding

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorSystem, Behavior}
import akka.cluster.sharding.typed.scaladsl.{ClusterSharding, Entity, EntityTypeKey}
import akka.util.Timeout
import com.typesafe.config.ConfigFactory

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.{Failure, Success}

object Main {

  val TypeKey = EntityTypeKey[sharding.grpc.Command]("Greeter")
  implicit val timeout = Timeout(5 seconds)

  object RootBehavior {
    def apply(port: Int): Behavior[Nothing] = Behaviors.setup[Nothing] { context =>
      // Create an actor that handles cluster domain events
      context.spawn(ClusterListener(), "ClusterListener")

      val sharding = ClusterSharding(context.system)

      sharding.init(
        Entity(TypeKey)(createBehavior = entityContext =>
          Greeter(entityContext.entityId, entityContext))
      )

      new GreeterServer(context.system, sharding).run(port + 1000)

      Behaviors.empty
    }
  }

  def main(args: Array[String]): Unit = {
    val ports =
      if (args.isEmpty)
        Seq(2551, 2552)
      else
        args.toSeq.map(_.toInt)
    ports.foreach(startup)
  }

  def startup(port: Int): Unit = {
    // Override the configuration of the port
    val config = ConfigFactory.parseString(s"""
      akka.remote.artery.canonical.port=$port
      """).withFallback(ConfigFactory.load())

    // Create an Akka system
    ActorSystem[Nothing](RootBehavior(port), "Hello", config)
  }

}
