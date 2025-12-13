package com.app.hld.load_balancer;

import java.util.concurrent.atomic.AtomicInteger;

public class Server {

    private final String id;
    private final int weight;
    private final AtomicInteger load = new AtomicInteger(0);

    public Server(String id, int weight) {
        this.id = id;
        this.weight = weight;
    }

    public String getId() {
        return id;
    }

    public int getWeight() {
        return weight;
    }

    public AtomicInteger getLoad() {
        return load;
    }

    public void incrementLoad() {
        load.incrementAndGet();
    }

    public void decrementLoad() {
        load.decrementAndGet();
    }

    public double effectiveLoad() {
        return (double) load.get() / Math.max(1, weight);
    }

}
