package akka.assign2
import akka.actor.{ ActorSystem, ActorLogging, Actor, Props }
import scala.io.Source
import akka.actor.ActorRef
import scala.concurrent.Await

case class Messege(msg: String)

class AtLeastOnceRec(ref: ActorRef) extends Actor {
  def receive = {
    case Messege("Messege") =>
      {
        ref ! Messege("ACK")

      }
      
  }
}

class AtLeastOnceSender extends Actor {

  var ackCounter = 0
  var maxAttempts = 5
  def receive = {
    case Messege("ACK") =>
      {
        ackCounter += 1
        println("Got ACK")
        if (maxAttempts > 0) {
          maxAttempts -= 1
          sender ! Messege("Messege")
        }
        
      }
  }

}

object AtleastOnce extends App {

  var maxSentAttempts = 5
  val system = ActorSystem("AtlestOnceActorSystem")
  val ALOSender = system.actorOf(Props[AtLeastOnceSender], "sender")
  val ALOReceiver = system.actorOf(Props(new AtLeastOnceRec(ALOSender)))
 println("---------------------calling At least Once---------------------")
  ALOReceiver ! Messege("Messege")
}