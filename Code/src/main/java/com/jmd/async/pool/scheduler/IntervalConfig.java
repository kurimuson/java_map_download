package com.jmd.async.pool.scheduler;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IntervalConfig {

    private int id;
    private Runnable task;
    private long mill;

    public IntervalConfig(int id, Runnable task, long mill) {
        this.id = id;
        this.task = task;
        this.mill = mill;
    }

}
