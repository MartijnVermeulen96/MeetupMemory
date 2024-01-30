package tech.vermeulen.meetup;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Class that extends {@link AllocateDirect} and uses 'newFixedThreadPool' to run all the memory allocations.
 */
public class AllocateDirectMultiThread extends AllocateDirect {
    private final ExecutorService executorService;
    private final ConcurrentLinkedQueue<Future<?>> futures;

    public AllocateDirectMultiThread(int numberOfAllocationsPerBatch, int minAllocationSize, int maxAllocationSize, int threadsPerAvailableProcessor) {
        super(numberOfAllocationsPerBatch, minAllocationSize, maxAllocationSize);

        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * threadsPerAvailableProcessor);
        futures = new ConcurrentLinkedQueue<>();
    }

    @Override
    protected void allocateDirect() {
        futures.add(executorService.submit(this::allocateAndFillDirect));
    }

    @Override
    protected void ensureAllocationsAreDone() throws ExecutionException, InterruptedException {
        while (!futures.isEmpty()) {
            futures.poll().get();
        }
    }
}
