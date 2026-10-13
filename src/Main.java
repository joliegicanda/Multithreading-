import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    public static void main(String[] args) {
        runTest(false);
        runTest(true);
    }

    public static void runTest(boolean safe) {

        if (safe) {
            System.out.println("=== WITH synchronized ===");
        } else {
            System.out.println("=== WITHOUT synchronized ===");
        }

        BankAccount[] accounts = new BankAccount[10];
        for (int i = 0; i < 10; i++) {
            accounts[i] = new BankAccount("User" + i, "ACC" + i, 10000);
        }

        ExecutorService pool = Executors.newFixedThreadPool(10);
        List<Future<String>> results = new ArrayList<>();

        long startTime = System.currentTimeMillis();

        for (int i = 1; i <= 100000; i++) {
            int from = (int) (Math.random() * 10);
            int to = (int) (Math.random() * 10);

            while (to == from) {
                to = (int) (Math.random() * 10);
            }

            int amount = (int) (Math.random() * 500) + 1;

            TransferTask task = new TransferTask(accounts[from], accounts[to], amount, safe);
            Future<String> future = pool.submit(task);
            results.add(future);
        }

        int successes = 0;
        int failures = 0;

        for (Future<String> future : results) {
            try {
                String message = future.get();

                if (message.equals("SUCCESS")) {
                    successes++;
                } else {
                    failures++;
                }
            } catch (InterruptedException e) {
                System.out.println("Task was interrupted.");
            } catch (ExecutionException e) {
                System.out.println("Task threw an error.");
            }
        }

        pool.shutdown();

        long endTime = System.currentTimeMillis();

        double total = 0;
        for (int i = 0; i < 10; i++) {
            total = total + accounts[i].getBalance();
        }

        System.out.println("Successful transfers: " + successes);
        System.out.println("Failed transfers:     " + failures);
        System.out.println("Expected total: 100000.0");
        System.out.println("Actual total:   " + total);
        System.out.println("Time taken:     " + (endTime - startTime) + " ms");

        if (total != 100000.0) {
            System.out.println("Race condition found!");
        } else {
            System.out.println("Total is correct.");
        }
        System.out.println();
    }
}