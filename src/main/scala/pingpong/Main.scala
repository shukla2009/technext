package pingpong

import akka.actor.{Props, ActorSystem, Actor}

/**
 * Created by Rahul Shukla <rahul.shukla@synerzip.com> on 8/10/15.
 */
object Main {

  def main(args: Array[String]) {
    val system = ActorSystem("technext")
    val pong = system.actorOf(Props[PongActor], name = "pong")
    val ping = system.actorOf(Props(new PingActor(pong)), name = "ping")
    ping ! Ping("Rahul Shukla")
  }

}
