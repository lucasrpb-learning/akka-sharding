package sharding

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorRefResolver, Behavior}
import akka.cluster.sharding.typed.scaladsl.EntityContext
import sharding.grpc._

object Greeter {

  //sealed trait Command extends CborSerializable
  //final case class Greet(name: String, replyTo: ActorRef[HelloResponse]) extends Command

  def apply(entityId: String, ectx: EntityContext[Command]) =
    Behaviors.setup[Command] { ctx =>

      val actorRefResolver = ActorRefResolver(ctx.system)

      Behaviors.receiveMessage[Command] {
        case HelloRequest(name, actorRef, _) =>

          val replyTo = actorRefResolver.resolveActorRef[HelloResponse](actorRef)

          replyTo ! HelloResponse(s"Hello, ${name} from shard ${ectx.shard}!")

          Behaviors.same
      }
  }

}
