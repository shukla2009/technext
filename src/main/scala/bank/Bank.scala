package bank

import akka.actor.SupervisorStrategy.{Restart, Resume}
import akka.actor._
import bank.Bank._

import scala.concurrent.duration._

/**
 * Created by Rahul Shukla <rahul.shukla@synerzip.com> on 8/10/15.
 */

object Bank {

  sealed trait BankMessage

  case class Balance(number: String) extends BankMessage

  case class OpenAccount(number: String) extends BankMessage

  case class Withdraw(number: String, amount: Int) extends BankMessage

  case class Deposit(number: String, amount: Int) extends BankMessage

  case class Transfer(from: String, to: String, amount: Int) extends BankMessage

  def props(name: String): Props = {
    return Props(Bank(name))
  }
}

case class Bank(name: String) extends AbstractLoggingActor {

  def createAccount(number: String) = {
    log.info(s"Creating Account with number $number")
    context.actorOf(Account.props(number), name = number)
  }

  def withdraw(number: String, amount: Int) = {
    context.actorSelection(number) ! Account.Withdraw(amount)
  }

  def deposit(number: String, amount: Int) = {
    context.actorSelection(number) ! Account.Deposit(amount)
  }

  def balance(number: String) = {
    context.actorSelection(number) ! Account.Balance
  }

  def transfer(from: String, to: String, amount: Int) = {
    log.info(s"Transfering $amount from $from to $to")
    context.actorSelection(from) ! Account.Transfer( to ,amount)
  }

  override def receive = {
    case OpenAccount(number) => createAccount(number)
    case Withdraw(number, amount) => withdraw(number, amount)
    case Deposit(number, amount) => deposit(number, amount)
    case Transfer(from, to, amount) => transfer(from, to, amount)
    case Balance(number) => balance(number)
  }

  override def supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
    case _: Account.OverdrawException => Resume
    case _: Exception => Restart
  }

}
