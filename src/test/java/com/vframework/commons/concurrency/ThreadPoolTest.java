package com.vframework.concurrency;

import java.util.ArrayDeque;
import java.util.Deque;

import org.junit.Before;
import org.junit.Test;

import com.vframework.util.PropertiesUtils;

public class ThreadPoolTest {
	
	private ThreadPool threadPool;
	private Deque<Runnable> threadQueue;
	
	@Before
	public void before() {
		threadQueue = new ArrayDeque<Runnable>();
		//new LinkedBlockingDeque<Runnable>();
		
		for(int i = 0; i < 1000; i ++) {
			threadQueue.add(new WorkerThread());
		}
		
		PropertiesUtils.loadTest();
	}
	
	@Test
	public void fixedThreadPoolTest() {
		threadPool = new ThreadPool(ThreadPoolType.FIXED, threadQueue);
		threadPool.execute();
	}
	
	@Test
	public void cachedThreadPoolTest() {
		threadPool = new ThreadPool(ThreadPoolType.CACHED, threadQueue);
		threadPool.execute();
	}
}
