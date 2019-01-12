package com.processor.cloud;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class CloudStorage {

	final String clientRegion = "ap-south-1";
	final String bucketName = "originals-upload";
	final String outputBucket = "resized-thumbs";
	private static final ThreadLocal<AmazonS3> threadLocal = new ThreadLocal<AmazonS3>();
	private static CloudStorage instance = null;
	final static Logger logger = Logger.getLogger(CloudStorage.class);

	private CloudStorage() {
        Logger.getRootLogger().setLevel(Level.INFO); 
	}

	public static CloudStorage getInstance() {
		if (instance == null) {
			synchronized (CloudStorage.class) {
				if (instance == null) {
					instance = new CloudStorage();
				}
			}
		}

		return instance;
	}

	private AmazonS3 gets3Connection() {
		AmazonS3 s3Client = threadLocal.get();
		 
        if (s3Client == null) {
            s3Client = AmazonS3ClientBuilder.standard().withRegion(clientRegion).withCredentials(new ProfileCredentialsProvider())
    				.build();
    		BasicConfigurator.configure(); 
            threadLocal.set(s3Client);
        }
        return s3Client;
	}
	
	// This function gets file from s3 and convert it into ImageInputStream
	public ImageInputStream getObjectFromCloud(String objName) throws IOException {

		ImageInputStream iis = null;
		S3Object fullObject = null;
		try {
			logger.info("Getting Object from Cloud");
			fullObject = gets3Connection().getObject(new GetObjectRequest(bucketName, objName));
			iis = ImageIO.createImageInputStream(fullObject.getObjectContent());
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (fullObject != null) {
				logger.info("Stream not closed may get memory leak");
			}
		}

		return iis;
	}
	// This function puts file to s3 and make it public
	public S3Object putObjectToCloud(BufferedImage objName, String fileName,String contentType) throws IOException {

		S3Object fullObject = null;
		File outputfile = null;
		try {
			outputfile = new File("/tmp/image-"+Thread.currentThread().getName()+ "-"+System.currentTimeMillis()+"-"+Math.random()+".jpg");
			ImageIO.write(objName, "jpg", outputfile);
			PutObjectRequest request = new PutObjectRequest(outputBucket, fileName, outputfile).withCannedAcl(CannedAccessControlList.PublicRead);
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType(contentType);
			request.setMetadata(metadata);
			gets3Connection().putObject(request);

		} catch(Exception ex) {
			logger.error("Exception while uploading file to cloud",ex);
		}
		finally {
			outputfile.delete();
		}

		return fullObject;
	}
	
	public static void main(String args[]) throws IOException {
		S3Object fullObject = null;
		AmazonS3 s3Client;
		try {
			s3Client = AmazonS3ClientBuilder.standard().withRegion("us-east-1").withCredentials(new ProfileCredentialsProvider())
					.build();
			BasicConfigurator.configure(); 
	        Logger.getRootLogger().setLevel(Level.INFO); 
			fullObject = s3Client.getObject(new GetObjectRequest("d11-banner-test", "asphalt-back-road-blurred-background-1546901.jpg"));
		} catch (Exception ex) {
			logger.error("Stream not closed may get memory leak",ex);
		} finally {
			if (fullObject != null) {
				fullObject.close();
			}
		}
	}

}
