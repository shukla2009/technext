package pingpong

import akka.actor.{ActorRef, Props, AbstractLoggingActor}
import akka.actor.Actor.Receive

/**
 * Created by Rahul Shukla <rahul.shukla@synerzip.com> on 8/10/15.
 */
case class Ping(whom: String) {
}

case class Terminate(whom: String) {
}


class PingActor(pong:ActorRef) extends AbstractLoggingActor {


  var counter = 0

  override def receive = {
    case Ping(whom) => {
      counter = counter + 1
      log.info(s"Ping by $whom ")
      pong ! Pong("Ping Actor")
      if (counter > 5) {
        self ! Terminate(self.toString())
      }
    }
    case Terminate(whom) => {
      log.info(s"Terminating by $whom ")
      context.stop(self)
    }
  }
}
