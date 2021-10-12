package com.toblers.origination.digital.lock;

public interface DistributedLock {

    public boolean lock(String key);

    public boolean release(String key);
}
