import akka.actor.{ ActorSystem, ActorLogging, Actor, Props }
import scala.io.Source
import akka.actor.ActorRef
import scala.concurrent.Await

case class Messege(msg: String)

class ExactlyOnceRec(ref: ActorRef) extends Actor {
  def receive = {
    case Messege("Messege") =>
      {
        ref ! Messege("ACK")
      }
    case Messege("ACK CNF") =>
      {
        println("ACK confirmed. stoping...")
        
      }
        context.stop(ref)  
  }
}

class ExactlyOnceSender extends Actor {

  def receive = {
    case Messege("ACK") =>
      {
          sender ! Messege("ACK CNF")
        }

      }
  
  }



object ExactlyOnce extends App {

  var maxSentAttempts = 5
  val system = ActorSystem("AtlestOnceActorSystem")
  val ALOSender = system.actorOf(Props[ExactlyOnceSender], "sender")
  val ALOReceiver = system.actorOf(Props(new ExactlyOnceRec(ALOSender)))
  println("---------------------calling Exactly Once---------------------")
  ALOReceiver ! Messege("Messege")
  
}