import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Random;

/**
 * The MasterThread class is responsible for the execution of slave threads to solve quadratic equations.
 * It manages the buffer, slave threads, and provides statistics about the computation of individual threads.
 */
public class MasterThread {
    private final Buffer buffer;    // Buffer to hold equations and their roots
    private final int numThreads;   // Number of slave threads
    private final int numCoefficients;  // Number of quadratic coefficients to process
    private final CountDownLatch latch; // Countdown latch to synchronize the completion of slave threads
    private final AtomicInteger solvedCount = new AtomicInteger(0); // Atomic counter to track total number of equations solved for
    private final ThreadStats[] threadStats;    // Array to hold statistics for each slave thread
    private final boolean is30Equations;    // Flag indicating whether to print additional information or not


    /**
     * Constructor for MasterThread that initializes the following parameters.
     *
     * @param buffer          The buffer for quadratic equations and their roots.
     * @param numThreads      The number of slave threads to be used to solve the quadratic equations.
     * @param numCoefficients The total number of sets of coefficients to solve with.
     * @param is30Equations   A flag indicating whether to print additional information specifically for if 30 equations
     *                        are to be processed.
     */
    public MasterThread(Buffer buffer, int numThreads, int numCoefficients, boolean is30Equations) {
        this.buffer = buffer;
        this.numThreads = numThreads;
        this.numCoefficients = numCoefficients;
        this.latch = new CountDownLatch(numThreads);
        this.threadStats = new ThreadStats[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threadStats[i] = new ThreadStats();
        }
        this.is30Equations = is30Equations;

    }

    /**
     * Starts the MasterThread. This method creates an executor service with a fixed number of threads. It generates
     * random coefficients for quadratic equations and adds them to the equation buffer. Launches slave threads to
     * solve the quadratic equations concurrently. It waits for all slave threads to finish their processing using a
     * CountDownLatch and measures the total time taken to solve all equations. It then shuts down the executor service,
     * allowing time for threads to complete their tasks.
     * Prints the total number of equations solved and, if 30 equations were solved, the roots for each equation.
     * If 3000 equations it prints statistics for each thread.
     *
     * @throws InterruptedException Throws if the thread is interrupted while waiting for slave threads to finish.
     */
    public void start() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        long startTime = System.currentTimeMillis();    // For keeping track of the total time to calculate all equations

        // Launch slave threads to solve equations
        for (int i = 0; i < numThreads; i++) {
            executor.execute(new SlaveThread(buffer, solvedCount, latch, i, threadStats));
        }


        // Adds random coefficients to the equation buffer to be solved
        Random random = new Random();
        for (int i = 0; i < numCoefficients; i++) {
            double a = random.nextDouble() * 10 - 5;
            double b = random.nextDouble() * 10 - 5;
            double c = random.nextDouble() * 10 - 5;
            // Add an equation to the equation buffer
            buffer.blockingPutEquation(new QuadraticEquation(a, b, c));
            Complex root = buffer.blockingGetRoots();
            if (root != null) {
                if (is30Equations) {
                    System.out.println("Root: " + root);
                }
            }
        }

        long endTime = System.currentTimeMillis();

        System.out.println("----------------------------------------------------------");
        System.out.println("All equations have been solved. Total solved: " + solvedCount.get());

        latch.await();  // Wait for all slave threads to finish


        executor.shutdownNow();    // Shut down the executor
        System.out.println("\nExecutorService shutdown initiated\n");
        if (executor.isShutdown()) {
            System.out.println("Executor terminated");
        }

        // Print thread statistics if number of equations is set to be 3000
        if (!is30Equations) {

            long totalTime = endTime - startTime;   // Calculate total time taken to process all equations
            for (int i = 0; i < numThreads; i++) {

                // Print how many equations a specific thread processed
                System.out.printf("Thread %d processed %d equations\n", i, threadStats[i].getEquationsProcessed());
                // Print out that threads average time to process an equation
                System.out.printf("Avg Time: %f milliseconds\n", threadStats[i].calcAvgTime(totalTime));
                System.out.println("----------------------------------");

            }

            System.out.printf("\nTotal time taken: %d milliseconds\n", totalTime);
        }
    }

    /**
     * Method to create a MasterThread instance based on the specified choice of number of equations.
     *
     * @param choice The choice indicating the number of coefficients (30 or 3000).
     * @return A MasterThread instance with the specified parameters.
     */
    public static MasterThread getMasterThread(int choice) {
        int numCoefficients;
        boolean is30Coefficients;
        if (choice == 1) {
            numCoefficients = 30;   // User chooses to process 30 equations
            is30Coefficients = true;
        } else {
            numCoefficients = 3000; // User chooses to process 3000 equations
            is30Coefficients = false;
        }

        // Create a new Buffer instance with
        Buffer buffer = new Buffer();
        // Return a new MasterThread instance with the created buffer, 10 threads, number of equations to process, and is30Coefficients flag
        return new MasterThread(buffer, 10, numCoefficients, is30Coefficients);
    }

    /**
     * The ThreadStats class holds statistics for each slave thread.
     */
    public static class ThreadStats {
        private int equationsProcessed;

        /**
         * Increments the number of equations processed by the thread.
         */
        public synchronized void incrementEquationsProcessed() {
            equationsProcessed++;
        }

        /**
         * Gets the number of equations processed by the thread.
         *
         * @return The number of equations processed.
         */
        public synchronized int getEquationsProcessed() {
            return equationsProcessed;
        }

        /**
         * Calculates the average time taken per equation processed by the slave thread.
         *
         * @param totalTime The total time taken by all threads to process the equations.
         * @return The average time per equation in milliseconds.
         */
        public synchronized double calcAvgTime(long totalTime) {
            if (equationsProcessed == 0) {
                return 0.0;
            }
            return (double) totalTime / equationsProcessed;
        }
    }
}
