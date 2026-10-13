import java.util.concurrent.Callable;

public class TransferTask implements Callable<String> {

    private BankAccount source;
    private BankAccount destination;
    private double amount;
    private boolean safe;

    public TransferTask(BankAccount source, BankAccount destination, double amount, boolean safe) {
        this.source = source;
        this.destination = destination;
        this.amount = amount;
        this.safe = safe;
    }

    @Override
    public String call() {
        boolean withdrawn;

        if (safe) {
            withdrawn = source.safeWithdraw(amount);
        } else {
            withdrawn = source.withdraw(amount);
        }

        if (withdrawn) {
            if (safe) {
                destination.safeDeposit(amount);
            } else {
                destination.deposit(amount);
            }
            return "SUCCESS";
        } else {
            return "FAILED";
        }
    }
}