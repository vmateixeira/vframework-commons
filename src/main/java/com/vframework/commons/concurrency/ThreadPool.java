package com.vframework.commons.concurrency;

import java.util.Deque;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
	private ExecutorService executor;
	private Deque<Runnable> queue;
	
	public ThreadPool(ThreadPoolType type, Deque<Runnable> queue) {
		this.queue = queue;
		
		switch(type) {
		case CACHED:
			executor = getCachedThreadPool();
			break;
		case FIXED:
			executor = getFixedThreadPool();
			break;
		default:
			break;
		}
	}
	
	public void execute() {
		Runnable worker = null;
		
		while((worker = queue.poll()) != null) {
			executor.execute(worker);
		}
		executor.shutdown();
		while(!executor.isTerminated());
	}
	
	private ExecutorService getCachedThreadPool() {
		return Executors.newCachedThreadPool();
	}
	
	private ExecutorService getFixedThreadPool() {
		Properties properties = System.getProperties();
		return Executors.newFixedThreadPool(Integer.parseInt(properties.getProperty("conn.pool.Max-Threads")));
	}
}

