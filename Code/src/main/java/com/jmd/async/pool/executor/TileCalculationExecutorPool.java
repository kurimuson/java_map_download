package com.jmd.async.pool.executor;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class TileCalculationExecutorPool {

    @Bean("TileCalculationExecutorPool")
    public TaskExecutor taskExecutor() {
        int count = Runtime.getRuntime().availableProcessors();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        if (count <= 2) {
            executor.setCorePoolSize(1);
        } else if (count <= 4) {
            executor.setCorePoolSize(2);
        } else if (count <= 6) {
            executor.setCorePoolSize(4);
        } else if (count <= 8) {
            executor.setCorePoolSize(4);
        } else {
            executor.setCorePoolSize(count / 2);
        }
        // 设置最大线程数
        if (count <= 2) {
            executor.setMaxPoolSize(1);
        } else if (count <= 4) {
            executor.setMaxPoolSize(3);
        } else if (count <= 6) {
            executor.setMaxPoolSize(4);
        } else {
            executor.setMaxPoolSize(count - 2);
        }
        // 设置队列容量
        // executor.setQueueCapacity(200);
        // 设置线程活跃时间（秒）
        // executor.setKeepAliveSeconds(60);
        // 设置默认线程名称
        executor.setThreadNamePrefix("TileCalculationExecutorPool-");
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }

}
