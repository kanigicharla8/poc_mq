package com.bnsf.drools.poc.channel;

import org.kie.api.runtime.Channel;

/**
 * Created by rakesh on 9/16/15.
 */
public abstract class AbstractBNSFChannel<T> implements Channel {
    public void send(Object object) {
        handle((T)object);
    }

    public abstract void handle(T obj);
}
