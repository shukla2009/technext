package bank

import akka.actor.{AbstractLoggingActor, Props}
import bank.Account._

/**
 * Created by Rahul Shukla <rahul.shukla@synerzip.com> on 8/10/15.
 */

/**
 * API for Account actor: Messages, exceptions etc.
 */
object Account {



  /** Base message type. */
  sealed trait AccountMessage

  case object Balance extends AccountMessage

  /** Message to disposit the given amount. */
  case class Deposit(amount: Int) extends AccountMessage

  /** Message to withdraw the given amount. */
  case class Withdraw(amount: Int) extends AccountMessage

  case class Transfer( to: String,amount: Int) extends AccountMessage

  /** Aborts a withdrawl. */
  class OverdrawException extends RuntimeException

  def props(name: String): Props = {
    return Props(Account(name))
  }
}

case class Account(name: String) extends AbstractLoggingActor {
  var balance = 0
  log.info(s"Account Created $name with path ${self.path}" )

  def deposit(amount: Int) = {
    balance = balance + amount
    log.info(s"Deposit Success for Account  : $name . Current balance is => $balance")
  }

  def withdraw(amount: Int) = {
    if (amount > balance) {
      log.warning("Account overdrawn! Aborting by throwing an OverdrawException.")
      throw new OverdrawException
    } else {
      balance = balance - amount
      log.info(s"Withdraw Success for Account  : $name . Current balance is => $balance")
    }
  }

  def transfer( to: String,amount: Int)={
    if (amount <= balance) {
      withdraw(amount)
      sender ! Bank.Deposit(to,amount)
    }else{
      log.warning("Account overdrawn! Aborting by throwing an OverdrawException.")
      throw new OverdrawException
    }

  }

  override def receive = {
    case Balance => log.info(s"Balance for Account : $name is => $balance")
    case Deposit(amount) => deposit(amount)
    case Withdraw(amount) => withdraw(amount)
    case Transfer(to,amount) => transfer(to,amount)
  }

}
