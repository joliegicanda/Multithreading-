public class BankAccount {

    private String id;
    private String name;
    private double balance;

    public BankAccount(String name, String id, double balance) {
        this.name = name;
        this.id = id;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance = balance + amount;
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance = balance - amount;
            return true;
        }
        return false;
    }

    public synchronized void safeDeposit(double amount) {
        if (amount > 0) {
            balance = balance + amount;
        }
    }

    public synchronized boolean safeWithdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance = balance - amount;
            return true;
        }
        return false;
    }
}