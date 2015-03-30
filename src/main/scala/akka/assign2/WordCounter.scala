package com.wordcount
import akka.actor.{ ActorSystem, ActorLogging, Actor, Props }
import scala.io.Source
import akka.actor.ActorRef
import akka.routing.RoundRobinPool
case class Line(line: String)
case class Messege(msg: String)

class Accumulator extends Actor with ActorLogging {
  var wordCounter = 0;

  val availableProcessors = Runtime.getRuntime.availableProcessors

  val childref = context.actorOf(RoundRobinPool(availableProcessors).props(Props[Child]))
  def receive = {
    case textfileName: String =>
      {
        try {
          for (line <- Source.fromFile(textfileName).getLines()) {
            childref ! Line(line)
          }
          childref ! Messege("End of File")
        } catch {
          case ex: Exception => println("File Not Found")
          childref ! Messege("End of File")
        }
      }
    case sumOfLine: Int =>
      {
        wordCounter += sumOfLine

      }
    case Messege("File Not Found") =>
      {
        println("File not found: please create a file name test.txt")
      }
    case Messege(messege) =>
      {
        println("WordCount: " + wordCounter)
      }
      context.stop(context.parent)
  }

}

class Child extends Actor with ActorLogging {

  def receive = {
    case Line(textLine) =>
      {
        if (textLine.length() > 0)
          sender ! textLine.split(" ").size
      }
    case Messege("End of File") =>
      sender ! Messege("End of File")
  }
}

object WordCounter extends App {
  val system = ActorSystem("WordCounter")
  val parentActor = system.actorOf(Props[Accumulator], "parent")
  println("------------------Counting words in the file--------------------------")
  parentActor ! "test.txt"
}
