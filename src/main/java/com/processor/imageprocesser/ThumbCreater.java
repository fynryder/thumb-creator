package com.processor.imageprocesser;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;

public class ThumbCreater {

	private ThumbCreater() {
		BasicConfigurator.configure(); 
        Logger.getRootLogger().setLevel(Level.INFO); 
	}

	private static ThumbCreater instance = null;

	public static ThumbCreater getInstance() {
		if (instance == null) {
			synchronized (ThumbCreater.class) {
				if (instance == null) {
					instance = new ThumbCreater();
				}
			}
		}

		return instance;
	}

	//This function creates thumb
	public BufferedImage createThumb(ImageInputStream imageStream, int maxH, int maxW) throws Exception {
		BufferedImage img = ImageIO.read(imageStream);
		BufferedImage scaledImg = Scalr.resize(img, Method.QUALITY, maxH, maxW, Scalr.OP_ANTIALIAS);
		return scaledImg;
	}

}
