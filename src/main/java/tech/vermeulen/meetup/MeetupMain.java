package tech.vermeulen.meetup;

import java.util.concurrent.ExecutionException;

/**
 * Main class for demonstration of what happens when allocating memory outside of the JVM.
 */
public class MeetupMain {
    /**
     * Assign max 256 (NUMBER_OF_ALLOCATIONS_PER_BATCH) * 4 Mb (MAX_ALLOCATION_SIZE) directly
     */
    private static final int NUMBER_OF_ALLOCATIONS_PER_BATCH = 256;
    private static final int MIN_ALLOCATION_SIZE = 1;
    private static final int MAX_ALLOCATION_SIZE = 4 * 1024 * 1024;
    private static final int LONG_WAIT_INTERVAL = 64;
    private static final int THREADS_PER_AVAILABLE_PROCESSOR = 4;


    public static void main(String[] args) throws InterruptedException, ExecutionException {
        if (args.length == 0 || !("single".equals(args[0]) || "multi".equals(args[0]))) {
            throw new IllegalArgumentException("Please set the first argument to `single` or `multi`");
        }

        System.out.printf("Starting with %d available processors.%n", Runtime.getRuntime().availableProcessors());

        // Pick the runner
        boolean singleThread = "single".equals(args[0]);
        AllocateDirect allocateDirect;
        if (singleThread) {
            allocateDirect = new AllocateDirectSingleThread(NUMBER_OF_ALLOCATIONS_PER_BATCH, MIN_ALLOCATION_SIZE, MAX_ALLOCATION_SIZE);
        } else {
            allocateDirect = new AllocateDirectMultiThread(NUMBER_OF_ALLOCATIONS_PER_BATCH, MIN_ALLOCATION_SIZE, MAX_ALLOCATION_SIZE, THREADS_PER_AVAILABLE_PROCESSOR);
        }

        int counter = 0;
        while (true) {

            // Allocate and free memory directly
            long start = System.currentTimeMillis();
            allocateDirect.allocateDirectBatch();
            allocateDirect.freeAll();
            System.out.printf("Bulk allocated and freed with %s, time taken: %d ms%n", singleThread ? "a single thread" : "multiple threads", System.currentTimeMillis() - start);

            // Garbage collect so we only really look at the directly allocated memory
            System.gc();


            // After LONG_WAIT_INTERVAL runs, wait a bit to see the results
            if (++counter >= LONG_WAIT_INTERVAL) {
                System.out.println("Waiting 10s");
                Thread.sleep(10000);
                counter = 0;
            }
        }
    }
}
