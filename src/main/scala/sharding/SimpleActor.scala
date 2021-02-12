package sharding

import akka.actor.typed.{ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object SimpleActor {

  def main(args: Array[String]): Unit = {

    def hello(): Behavior[String] = Behaviors.setup { ctx =>

      println("Hello!!!")

      Behaviors.stopped
    }

    val system = ActorSystem[String](hello(), "Hello")

    Await.ready(system.whenTerminated, Duration.Inf)

  }

}
