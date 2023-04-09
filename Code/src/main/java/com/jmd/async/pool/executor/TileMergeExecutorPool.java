package com.jmd.async.pool.executor;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class TileMergeExecutorPool {

	private ThreadPoolTaskExecutor executor;

	@Bean("TileMergeExecutorPool")
	public TaskExecutor taskExecutor() {
		int count = Runtime.getRuntime().availableProcessors();
		executor = new ThreadPoolTaskExecutor();
		// 设置核心线程数
		executor.setCorePoolSize(count);
		// 设置最大线程数
		executor.setMaxPoolSize(count);
		// 设置队列容量
		// executor.setQueueCapacity(200);
		// 设置线程活跃时间（秒）
		// executor.setKeepAliveSeconds(60);
		// 设置默认线程名称
		executor.setThreadNamePrefix("TileMergeExecutorPool-");
		// 设置拒绝策略
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		// 等待所有任务结束后再关闭线程池
		executor.setWaitForTasksToCompleteOnShutdown(true);
		return executor;
	}

	public int getActiveCount() {
		return executor.getActiveCount();
	}

}
