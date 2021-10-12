package com.toblers.origination.digital.lock.redis;

import com.toblers.origination.digital.lock.DistributedLock;

public class RedisDistributedLock implements DistributedLock {
    @Override
    public boolean lock(String key) {
        return false;
    }

    // Release is redundant
    @Override
    public boolean release(String key) {
        return false;
    }
}
