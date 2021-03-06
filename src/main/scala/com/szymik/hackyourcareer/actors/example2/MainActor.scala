package com.szymik.hackyourcareer.actors.example2

import akka.actor.{Actor, ActorLogging, PoisonPill, Props}
import com.szymik.hackyourcareer.actors.example2.CalculationManager.{Calculate, CalculationResult}

object MainActor {

  /**
    * Returns props for main actor.
    *
    * @return
    */
  def props(): Props = Props(new MainActor())
}

/**
  * Main actor which is initializing sending greetings.
  */
class MainActor extends Actor with ActorLogging {

  val HowManyCalculation = 20

  override def preStart(): Unit = {
    val calculationManager = context.actorOf(CalculationManager.props(), "calculationManagerActor")
    calculationManager ! Calculate(HowManyCalculation)
  }

  override def receive: Receive = {

    case CalculationResult(results) ⇒
      val resultsMessage = buildResponseWithValues(results)
      log.info(resultsMessage)

      self ! PoisonPill
  }

  private def buildResponseWithValues(results: Map[Int, BigInt]): String = {
    results.map { case (index, value) ⇒ // .toSeq.sortBy(_._1)
      s"\nCalculation number $index returned $value"
    }.mkString(", ")
  }

}
