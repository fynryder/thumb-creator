package com.processor.job;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Runner {

	ExecutorService executorService;

	private Runner() {
		executorService = new ThreadPoolExecutor(2, 2, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		BasicConfigurator.configure(); 
        Logger.getRootLogger().setLevel(Level.INFO); 
	}

	private static Runner instance = null;

	public static Runner getInstance() {

		if (instance == null) {
			synchronized (Runner.class) {
				if (instance == null) {
					instance = new Runner();
				}
			}
		}

		return instance;
	}

	public void startProcessingImage(ImageProcessorTask task) {
		executorService.submit(task);
	}

}
