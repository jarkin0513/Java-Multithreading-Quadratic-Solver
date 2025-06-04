import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class SlaveThread implements Runnable {
    private final Buffer buffer;    // Buffer for SlaveThread to retrieve equations and to place corresponding roots into
    private final AtomicInteger solvedCount;    // To keep track of total number of equations solved for
    private final int id;   // Identifier to distinguish between different threads
    private final CountDownLatch latch;     // Used to synchronize the completion of SlaveThread instances
    private final MasterThread.ThreadStats[] threadStats;   // Array of ThreadStat objects to track information about each SlaveThread

    /**
     * Constructor for SlaveThread.
     *
     * @param buffer        The buffer from which the SlaveThread retrieves QuadraticEquation objects and roots get place.
     * @param solvedCount   The AtomicInteger to keep track of the total number of equations solved.
     * @param latch         The CountDownLatch to synchronize the completion of SlaveThreads.
     * @param id            The identifier of the SlaveThread.
     * @param threadStats   The array of ThreadStats objects to track information for each SlaveThread.
     */
    public SlaveThread(Buffer buffer, AtomicInteger solvedCount, CountDownLatch latch, int id,
                       MasterThread.ThreadStats[] threadStats) {
        this.buffer = buffer;
        this.solvedCount = solvedCount;
        this.id = id;
        this.latch = latch;
        this.threadStats = threadStats;
    }

    /**
     * The run method executes the processing logic for the SlaveThread. This method is called when the thread is started.
     * The SlaveThread retrieves quadratic equations from a shared buffer, solves them, and stores the resulting
     * roots back into the buffer. Thread-specific statistics are updated, and the CountDownLatch is used for
     * synchronization to signal the completion of equation processing. This method continues running until the equation
     * buffer is empty. The method handles InterruptedException by interrupting the current thread
     * if an interruption occurs during execution.
     */
    @Override
    public void run() {
        try {
            // Continue processing equations until the equation buffer is empty
            while (true) {

                // Retrieve a quadratic equation from the buffer
                QuadraticEquation equation = buffer.blockingGetEquation();
                if (equation == null) {
                    // If buffer empty exit loop
                    break;
                }

                // Solve the equation and retrieve the roots
                Complex roots = equation.getRoots();

                // Add roots to buffer
                buffer.blockingPutRoots(roots);

                // Increment number of solvedCount and retrieve the updated value
                solvedCount.incrementAndGet();

                // Update stats for current thread
                threadStats[id].incrementEquationsProcessed();

                // Decrease the count of the latch to indicate processing completed for current equation
                latch.countDown();

            }


        } catch (InterruptedException e) {
            // Interrupt current thread if InterruptedException is thrown
            Thread.currentThread().interrupt();
        }
    }


}

