package com.jmd.async.pool.scheduler;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import com.jmd.rx.Topic;
import com.jmd.rx.service.InnerMqService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;

@Component
public class IntervalTaskSchedulerPool {

    private final Map<Integer, ScheduledFuture<?>> intervalMap = new HashMap<>();
    private final InnerMqService innerMqService = InnerMqService.getInstance();

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    @PostConstruct
    private void init() {
        try {
            this.subInnerMqMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Async("IntervalTaskSchedulerPool")
    public ScheduledFuture<?> setInterval(Runnable task, int secInterval) {
        return taskScheduler.schedule(task, (arg0) -> {
            var corn = "*/" + secInterval + " * * * * ?";
            return new CronTrigger(corn).nextExecution(arg0);
        });
    }

    @Async("IntervalTaskSchedulerPool")
    public ScheduledFuture<?> setInterval(Runnable task, long millInterval) {
        var periodicTrigger = new PeriodicTrigger(Duration.ofMillis(millInterval));
        periodicTrigger.setFixedRate(true);
        periodicTrigger.setInitialDelay(Duration.ofMillis(millInterval));
        return taskScheduler.schedule(task, periodicTrigger);
    }

    public boolean clearInterval(ScheduledFuture<?> future) {
        return future.cancel(true);
    }

    private void subInnerMqMessage() throws Exception {
        var client = this.innerMqService.createClient();
        client.<IntervalConfig>sub(Topic.SET_INTERVAL, (config) -> {
            if (intervalMap.get(config.getId()) == null) {
                ScheduledFuture<?> future = setInterval(config.getTask(), config.getMill());
                intervalMap.put(config.getId(), future);
            }
        });
        client.<Integer>sub(Topic.CLEAR_INTERVAL, (id) -> {
            ScheduledFuture<?> future = intervalMap.get(id);
            if (future != null) {
                clearInterval(future);
                intervalMap.remove(id);
            }
        });
    }

}
