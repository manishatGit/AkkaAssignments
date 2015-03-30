package akka.assign2
package com.wordcount
import akka.actor.{ ActorSystem, ActorLogging, Actor, Props }
import scala.io.Source
import akka.actor.ActorRef
import akka.actor.Kill

case class Messege(msg: String)

class FireAndForgetSender extends Actor {

  def receive = {
    case Messege("Reply Messege") =>
      {
        println("----Got Reply-----")
        Messege("Stop Messege")

      }
    case Messege("Normal Messege") =>
      {
        println("----Got Normal messege----: ")

        sender ! Messege("Reply Messege")

      }
    case Messege("Stop Messege") =>
      {
        println("----Got Stop messege----: ")
        println("Stopping...")
        context.stop(context.parent)  
      }
      
  }
  override def toString() = "FireAndForgetSender"
}

object FireAndForget extends App {
  val system = ActorSystem("FireandForget")
  val fireAndForgetSenderOneActor = system.actorOf(Props(new FireAndForgetSender), "FireAndForgetSenderOne")
   println("---------------------calling fire and forget---------------------")
  fireAndForgetSenderOneActor ! Messege("Normal Messege")
  fireAndForgetSenderOneActor ! Messege("Stop Messege")
}




