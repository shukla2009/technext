package bank

import akka.actor.ActorSystem

/**
 * Created by Rahul Shukla <rahul.shukla@synerzip.com> on 8/10/15.
 */
object BankApp extends App {

  val system = ActorSystem("Bank")
  val bank = system.actorOf(Bank.props("Abc Bank"))

  println("####################### Welcome ############################")


  def action(ln: String) = {
    ln.split(" ").head.trim match {
      case "A" => bank ! Bank.OpenAccount(ln.split(" ")(1))
      case "B" => bank ! Bank.Balance(ln.split(" ")(1))
      case "D" => bank ! Bank.Deposit(ln.split(" ")(1), ln.split(" ")(2).toInt)
      case "W" => bank ! Bank.Withdraw(ln.split(" ")(1), ln.split(" ")(2).toInt)
      case "T" => bank ! Bank.Transfer(ln.split(" ")(1), ln.split(" ")(2), ln.split(" ")(3).toInt)
      case _ => println("Not a Valid Command")
    }

  }

  for (ln <- io.Source.stdin.getLines) action(ln)


  //  bank ! Bank.OpenAccount("1234")
  //  bank ! Bank.OpenAccount("1235")
  //  bank ! Bank.Deposit("1234", 1000)
  //  bank ! Bank.Transfer("1234", "1235", 500)
  //  bank ! Bank.Balance("1235")
  //  bank ! Bank.Withdraw("1234", 1000)
  //  bank ! Bank.Withdraw("1235", 500)
  //  bank ! Bank.Balance("1234")

}
