package com.processor.db;

import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.processor.Entities.ProcessedImage;
import com.processor.utils.Constants;

public class DataAccessObject {

	private static DataAccessObject instance = new DataAccessObject();
	final static Logger logger = Logger.getLogger(DataAccessObject.class);
	
	private DataAccessObject() {
		 Logger.getRootLogger().setLevel(Level.INFO);
	}

	public static DataAccessObject getInstance() {
		return instance;
	}
	
	public List<ProcessedImage> getUnProcessedImages() {
		return null;
	}

	//This functions set status of processed image after success upload to s3
	public void setImagesProcessed(long imageId,String fileName) {
		logger.info("Saving to Database "+imageId);
		Session sessionObj = DBAccessor.getInstance().getSessionFactory().openSession();
		try {
			ProcessedImage entity = (ProcessedImage) sessionObj.get(ProcessedImage.class, imageId);
			System.out.println("Entity is " + entity);
			if (entity != null) {
				entity.setStatus(Constants.SUCCESS);
				entity.setProcessedImage(fileName);
				sessionObj.beginTransaction();
				sessionObj.save(entity);
				sessionObj.getTransaction().commit();				
			}
		} catch (Exception ex) {
			logger.error("Exception while setting data to database",ex);
		}
	}
}
