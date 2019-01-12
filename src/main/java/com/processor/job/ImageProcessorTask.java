package com.processor.job;

import java.awt.image.BufferedImage;

import javax.imageio.stream.ImageInputStream;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.processor.cloud.CloudStorage;
import com.processor.db.DataAccessObject;
import com.processor.imageprocesser.ThumbCreater;

public class ImageProcessorTask implements Runnable {

	private long rowId;
	private String s3Path;
	private int maxH;
	private int maxW;
	private ImageInputStream s3ImageFile;
	private String contentType;
	final static Logger logger = Logger.getLogger(ImageProcessorTask.class);

	public ImageProcessorTask(int rowId, String s3Path, int maxH, int maxW, ImageInputStream s3ImageFile, String contentType) {
		this.rowId = rowId;
		this.s3Path = s3Path;
		this.maxH = maxH;
		this.maxW = maxW;
		this.s3ImageFile = s3ImageFile;
		this.contentType = contentType;
		
		BasicConfigurator.configure(); 
        Logger.getRootLogger().setLevel(Level.INFO); 
	}

	public void run() {
		
		try {
			logger.info("Image Processsing......");
			//Transform Image and save it to s3
			BufferedImage transformedImage = ThumbCreater.getInstance().createThumb(s3ImageFile, maxH, maxW);
			CloudStorage.getInstance().putObjectToCloud(transformedImage, s3Path, contentType);
			
			//Set Image in DB
			DataAccessObject.getInstance().setImagesProcessed(rowId,s3Path);
		} catch (Exception e) {
			logger.error("Error while processing image ",e);
		} 
	}

	public long getRowId() {
		return rowId;
	}

	public void setRowId(long rowId) {
		this.rowId = rowId;
	}

	public String getS3Path() {
		return s3Path;
	}

	public void setS3Path(String s3Path) {
		this.s3Path = s3Path;
	}

	public int getMaxH() {
		return maxH;
	}

	public void setMaxH(int maxH) {
		this.maxH = maxH;
	}

	public int getMaxW() {
		return maxW;
	}

	public void setMaxW(int maxW) {
		this.maxW = maxW;
	}
}

