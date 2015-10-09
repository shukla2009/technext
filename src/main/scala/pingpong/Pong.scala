package pingpong

import akka.actor.AbstractLoggingActor

/**
 * Created by Rahul Shukla <rahul.shukla@synerzip.com> on 8/10/15.
 */
case class Pong(whom: String) {

}

class PongActor extends AbstractLoggingActor {

  override def receive = {
    case Pong(whom) => {
      log.info(s"Pong by $whom "); sender ! Ping("Pong Actor")
    }
  }
}
