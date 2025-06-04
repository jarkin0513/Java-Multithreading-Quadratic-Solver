/**
 * Buffer represents a class that holds quadratic equations and their corresponding roots. This class provides methods
 * for adding equations and roots to the buffer, as well as retrieving them in a synchronized manner.
 */
public class Buffer {
    private final QuadraticEquation[] equationBuffer;
    private final Complex[] rootsBuffer;
    private int writeEquationIndex;
    private int readEquationIndex;
    private int writeRootsIndex;
    private int readRootsIndex;
    private int occupiedEquationCells;
    private int occupiedRootsCells;

    public Buffer() {
        int bufferSize = 10;
        this.equationBuffer = new QuadraticEquation[bufferSize];
        this.rootsBuffer = new Complex[bufferSize];
        this.writeEquationIndex = 0;
        this.readEquationIndex = 0;
        this.writeRootsIndex = 0;
        this.readRootsIndex = 0;
        this.occupiedEquationCells = 0;
        this.occupiedRootsCells = 0;
    }

    /**
     * Adds a quadratic equation to the buffer. If the buffer is full, the method waits until space becomes available.
     * It updates the write index and notifies waiting threads.
     * Uses the 'synchronized' keyword to ensure thread safety.
     * @param equation The quadratic equation to add to the buffer.
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    public synchronized void blockingPutEquation(QuadraticEquation equation) throws InterruptedException {
        // Wait for space in buffer
        while (occupiedEquationCells == equationBuffer.length) {
            wait();
        }

        // Add the equation to the buffer
        equationBuffer[writeEquationIndex] = equation;

        // Write index gets incremented and if it exceeds buffer size, it wraps around to the beginning to reuse space in the buffer
        writeEquationIndex = (writeEquationIndex + 1) % equationBuffer.length;

        // Increment occupiedCells count that keeps track of how many positions in the buffer are full
        ++occupiedEquationCells;

        // Notify all waiting threads that the buffer has been updated
        notifyAll();
    }

    /**
     * Retrieves a quadratic equation from the buffer. If the buffer is empty, the method waits until an equation
     * becomes available. It updates the read index and notifies waiting threads.
     * Uses the 'synchronized' keyword to ensure thread safety.
     * @return The retrieved quadratic equation.
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    public synchronized QuadraticEquation blockingGetEquation() throws InterruptedException {
        // Wait until there is an equation in the buffer
        while (occupiedEquationCells == 0) {
            wait();
        }

        // Retrieve and remove equation from the buffer
        QuadraticEquation equation = equationBuffer[readEquationIndex];

        // Mark corresponding position as null to make room for new equation
        equationBuffer[readEquationIndex] = null;

        // Read index gets incremented and if it exceeds the buffer size, wraps around to beginning to reuse space in the buffer
        readEquationIndex = (readEquationIndex + 1) % equationBuffer.length;

        // Decrement occupiedCells count that keeps track of how many positions in the buffer are full
        --occupiedEquationCells;

        // Notify all waiting threads that the buffer has been updated
        notifyAll();

        // Return the retrieved equation
        return equation;
    }

    /**
     * Adds roots to the buffer. If the buffer is full, the method waits until space becomes available.
     * It updates the write index and notifies waiting threads.
     * Uses the 'synchronized' keyword to ensure thread safety.
     * @param roots The roots to add to the buffer.
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    public synchronized void blockingPutRoots(Complex roots) throws InterruptedException {
        // Wait until there is space in buffer
        while (occupiedRootsCells == rootsBuffer.length) {
            wait();
        }

        // Add the roots to the buffer
        rootsBuffer[writeRootsIndex] = roots;

        // Update the write index with same logic as other put method
        writeRootsIndex = (writeRootsIndex + 1) % rootsBuffer.length;

        // Increment occupiedCells count that keeps track of how many positions in the buffer are full
        ++occupiedRootsCells;

        // Notify all waiting threads that the buffer has been updated
        notifyAll();
    }

    /**
     * Retrieves roots from the buffer. If the buffer is empty, it waits until roots are available.
     * It updates the read index and notifies waiting threads.
     * Uses the 'synchronized' keyword to ensure thread safety.
     * @return The retrieved roots.
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    public synchronized Complex blockingGetRoots() throws InterruptedException {
        // Wait until there are roots in the buffer
        while (rootsBuffer[readRootsIndex] == null) {
            wait();
        }

        // Retrieve and remove roots from buffer
        Complex roots = rootsBuffer[readRootsIndex];

        // Mark corresponding position as null to make room for new roots
        rootsBuffer[readRootsIndex] = null;

        // Update the read index with same logic as other get method
        readRootsIndex = (readRootsIndex + 1) % rootsBuffer.length;

        // Decrement occupiedCells count that keeps track of how many positions in the buffer are full
        --occupiedRootsCells;

        // Notify waiting threads that the buffer has been updated
        notifyAll();

        // Return retrieved roots
        return roots;
    }

}
