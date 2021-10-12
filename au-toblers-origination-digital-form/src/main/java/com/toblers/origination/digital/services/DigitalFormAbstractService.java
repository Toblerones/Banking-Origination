package com.toblers.origination.digital.services;

import com.toblers.origination.digital.lock.DistributedLock;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class DigitalFormAbstractService implements DigitalFormService{
    @Autowired
    public DistributedLock distributedLock;

    protected boolean lock(String key){
        return distributedLock.lock(key);
    };
    protected boolean release(String key){
        return distributedLock.release(key);
    };
}
