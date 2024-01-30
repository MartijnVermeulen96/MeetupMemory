package tech.vermeulen.meetup;

/**
 * Class that extends {@link AllocateDirect} and just uses a synchronous call to {@link this#allocateAndFillDirect()}
 */
public class AllocateDirectSingleThread extends AllocateDirect {
    public AllocateDirectSingleThread(int numberOfAllocationsPerBatch, int minAllocationSize, int maxAllocationSize) {
        super(numberOfAllocationsPerBatch, minAllocationSize, maxAllocationSize);
    }

    @Override
    protected void allocateDirect() {
        allocateAndFillDirect();
    }

    @Override
    void ensureAllocationsAreDone() {
        // Nothing to ensure, as it is all single-threaded and synchronous
    }
}
