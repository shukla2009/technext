package bank1;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Rahul Shukla <rahul.shukla@synerzip.com> on 9/10/15.
 */
public class BankApp {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("Bank");
        ActorRef bank = system.actorOf(Bank.props("Abc Bank"));
        System.out.println("####################### Welcome Java Version############################");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            while (true) {
                String command = reader.readLine();
                action(command, bank);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void action(String command, ActorRef bank) {
        String[] cmds = command.split(" ");

        switch (cmds[0]) {
            case "A":
                bank.tell(new Bank.OpenAccount(cmds[1]), ActorRef.noSender());
                break;
            case "B":
                bank.tell(new Bank.Balance(cmds[1]), ActorRef.noSender());
                break;
            case "D":
                bank.tell(new Bank.Deposit(cmds[1], Integer.parseInt(cmds[2])), ActorRef.noSender());
                break;
            case "W":
                bank.tell(new Bank.Withdraw(cmds[1], Integer.parseInt(cmds[2])), ActorRef.noSender());
                break;
            case "T":
                bank.tell(new Bank.Transfer(cmds[1], cmds[2], Integer.parseInt(cmds[3])), ActorRef.noSender());
                break;

            default:
                System.out.println("Not a Valid Command");


        }
    }
}
