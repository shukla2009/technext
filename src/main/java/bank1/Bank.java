package bank1;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

/**
 * Created by Rahul Shukla <rahul.shukla@synerzip.com> on 9/10/15.
 */
public class Bank extends AbstractLoggingActor {

    private final String name;

    public Bank(String name) {

        this.name = name;
    }

    static interface BankMessage {
    }

    static class Balance implements BankMessage {
        public final String number;

        public Balance(String number) {
            this.number = number;
        }
    }

    static class OpenAccount implements BankMessage {
        public final String number;

        public OpenAccount(String number) {
            this.number = number;
        }
    }

    static class Withdraw implements BankMessage {
        public final String number;
        public final int amount;

        public Withdraw(String number, int amount) {
            this.number = number;
            this.amount = amount;
        }
    }

    static class Deposit implements BankMessage {
        public final String number;
        public final int amount;

        public Deposit(String number, int amount) {
            this.number = number;
            this.amount = amount;
        }
    }

    static class Transfer implements BankMessage

    {
        public final String from;
        public final String to;
        public final int amount;

        public Transfer(String from, String to, int amount) {
            this.from = from;
            this.to = to;
            this.amount = amount;
        }
    }

    public static Props props(String name) {
        return Props.create(Bank.class, () -> new Bank(name));
    }

    @Override
    public PartialFunction<Object, BoxedUnit> receive() {
        return ReceiveBuilder
                .match(Balance.class, b -> balance(b.number))
                .match(Deposit.class, d -> deposit(d.number, d.amount))
                .match(Withdraw.class, w -> withdraw(w.number, w.amount))
                .match(Transfer.class, t -> transfer(t.from, t.to, t.amount))
                .match(OpenAccount.class, a -> create(a.number))
                .build();
    }

    private void create(String number) {
        getContext().actorOf(Account.props(number), number);
    }

    private void balance(String number) {
        getContext().actorSelection(number).tell(new Account.Balance(), self());
    }

    private void transfer(String from, String to, int amount) {
        getContext().actorSelection(from).tell(new Account.Transfer(to, amount), self());
    }

    private void withdraw(String number, int amount) {
        getContext().actorSelection(number).tell(new Account.Withdraw(amount), self());
    }

    private void deposit(String number, int amount) {
        getContext().actorSelection(number)
                .tell(new Account.Deposit(amount), self());
    }
}
