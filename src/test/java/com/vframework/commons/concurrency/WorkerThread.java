package com.vframework.concurrency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkerThread implements Runnable {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WorkerThread.class);
	
	@Override
	public void run() {
		LOGGER.info("Run!");
	}
}
