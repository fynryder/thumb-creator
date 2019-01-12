package com.processor.reader;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.imageio.stream.ImageInputStream;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.processor.cloud.CloudStorage;
import com.processor.job.ImageProcessorTask;
import com.processor.job.Runner;
import com.processor.utils.Constants;

public class MessageProcessor {

	private static final MessageProcessor instance = new MessageProcessor();
	ExecutorService executorService = null;

	public MessageProcessor() {
		executorService = new ThreadPoolExecutor(2, 2, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
	}

	public static MessageProcessor getInstance() {
		return instance;
	}

	public void processMessage(String message) {
		KakfaTask task = new KakfaTask(message);
		executorService.submit(task);

	}

}

class KakfaTask implements Runnable {

	String message;
	final static Logger logger = Logger.getLogger(KakfaTask.class);
	public KakfaTask(String message) {
		this.message = message;
		BasicConfigurator.configure(); 
        Logger.getRootLogger().setLevel(Level.INFO); 
	}

	@Override
	public void run() {
		try {
			logger.info("Running... : " +  message);
			JSONObject imageObj = new JSONObject(message);

			String imagePath = imageObj.getString(Constants.KEY_S3_PATH);
			int mHeight = imageObj.optInt(Constants.RESIZE_HEIGHT);
			int mWidth = imageObj.optInt(Constants.RESIZE_WIDTH);
			int rowId = imageObj.optInt(Constants.ROW_ID);

			ImageInputStream imageObject = CloudStorage.getInstance().getObjectFromCloud(imagePath);
			String contentType = Constants.CONTENT_TYPE;
			
			ImageProcessorTask task = new ImageProcessorTask( rowId,  imagePath,  mHeight,  mWidth,  imageObject,  contentType);
			Runner.getInstance().startProcessingImage(task);
			
		} catch (Exception ex) {
			logger.error("Error In kafkatask "+ex);
		}

	}

}