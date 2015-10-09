package bank1;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

/**
 * Created by Rahul Shukla <rahul.shukla@synerzip.com> on 9/10/15.
 */
public class Account extends AbstractLoggingActor {

    private final String name;
    private int balance = 0;

    public Account(String name) {
        this.name = name;
        log().info("Account Created " + name + "with path " + self().path());
    }

    /**
     * Base message type.
     */
    static interface AccountMessage {
    }

    static class Balance implements AccountMessage {
    }

    /**
     * Message to disposit the given amount.
     */
    static class Deposit implements AccountMessage {
        public final int amount;

        public Deposit(int amount) {
            this.amount = amount;
        }
    }

    /**
     * Message to withdraw the given amount.
     */
    static class Withdraw implements AccountMessage {
        public final int amount;

        public Withdraw(int amount) {

            this.amount = amount;
        }
    }

    static class Transfer implements AccountMessage {
        public final String to;
        public final int amount;

        public Transfer(String to, int amount) {
            this.to = to;
            this.amount = amount;
        }
    }

    /**
     * Aborts a withdrawl.
     */
    static class OverdrawException extends RuntimeException {
    }

    public static Props props(String name) {
        return Props.create(Account.class, () -> new Account(name));
    }


    private void deposit(int amount) {
        balance = balance + amount;
        log().info("Deposit Success for Account  : " + name + ". Current balance is => " + balance);
    }

    private void withdraw(int amount) {
        if (amount > balance) {
            log().warning("Account overdrawn! Aborting by throwing an OverdrawException.");
            throw new OverdrawException();
        } else {
            balance = balance - amount;
            log().info("Withdraw Success for Account  : " + name + ". Current balance is => " + balance);
        }
    }

    @Override
    public PartialFunction<Object, BoxedUnit> receive() {
        return ReceiveBuilder
                .match(Balance.class, b -> log().info("Balance for Account :" + name + " is =>" + balance))
                .match(Deposit.class, d -> deposit(d.amount))
                .match(Withdraw.class, w -> withdraw(w.amount))
                .match(Transfer.class, t -> transfer(t.to, t.amount))
                .build();
    }

    private void transfer(String to, int amount) {
        if(balance >= amount){
            withdraw(amount);
            sender().tell(new Bank.Deposit(to,amount),self());
        }
    }


}
