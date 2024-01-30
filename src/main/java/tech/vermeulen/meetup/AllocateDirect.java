package tech.vermeulen.meetup;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Abstract class to do direct memory allocations using {@link Unsafe}.
 * Only for demonstration purposes.
 */
public abstract class AllocateDirect {
    private final int numberOfAllocationsPerBatch;
    private final int minAllocationSize;
    private final int maxAllocationSize;
    private final ConcurrentLinkedQueue<Long> allocatedAddresses;
    private final Unsafe unsafe;

    public AllocateDirect(int numberOfAllocationsPerBatch, int minAllocationSize, int maxAllocationSize) {
        this.numberOfAllocationsPerBatch = numberOfAllocationsPerBatch;
        this.minAllocationSize = minAllocationSize;
        this.maxAllocationSize = maxAllocationSize;
        allocatedAddresses = new ConcurrentLinkedQueue<>();

        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract void allocateDirect();

    abstract void ensureAllocationsAreDone() throws ExecutionException, InterruptedException;


    /**
     * Simple loop to call the abstract {@link this#allocateDirect()}
     */
    public final void allocateDirectBatch() {
        for (int i = 0; i < numberOfAllocationsPerBatch; i++) {
            allocateDirect();
        }
    }

    /**
     * Checks if all we are ready with all the allocations.
     * If so, we will go through the queue and free the allocated memory at those addresses again.
     *
     * @throws ExecutionException   if the ensureAllocationsAreDone can't finish properly
     * @throws InterruptedException if the ensureAllocationsAreDone can't finish properly
     */
    public final void freeAll() throws ExecutionException, InterruptedException {
        ensureAllocationsAreDone();

        while (!allocatedAddresses.isEmpty()) {
            Long address = allocatedAddresses.poll();
            unsafe.freeMemory(address);
        }
    }


    /**
     * Allocates memory of a random size between minAllocationSize and maxAllocationSize
     * using {@link Unsafe} and fill some bytes. Adds the address to the queue.
     */
    protected final void allocateAndFillDirect() {
        int allocSize = ThreadLocalRandom.current().nextInt(minAllocationSize, maxAllocationSize);

        long address = unsafe.allocateMemory(allocSize);
        unsafe.putByte(address, (byte) 1);
        unsafe.putByte(address + (allocSize - 1), (byte) 1);
        allocatedAddresses.add(address);
    }
}
