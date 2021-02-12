package sharding

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import akka.cluster.sharding.typed.scaladsl.EntityContext
import sharding.grpc.HelloResponse

object Greeter {

  sealed trait Command

  final case class Greet(name: String, replyTo: ActorRef[HelloResponse]) extends Command

  def apply(entityId: String, ectx: EntityContext[Command]) = Behaviors.setup[Greeter.Command] { ctx =>

    Behaviors.receiveMessage[Command] {
      case Greet(name, replyTo) =>

        replyTo ! HelloResponse(s"Hello, ${name} from shard ${ectx.shard}!")

        Behaviors.same
    }
  }

}
