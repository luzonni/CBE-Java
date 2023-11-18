package com.coffee.graphics;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Flip {
	
	public static BufferedImage Horizontal(BufferedImage image) {
		return createFlippedY(image);
	}
	
	private static BufferedImage createFlippedY(BufferedImage image) {
        AffineTransform at = new AffineTransform();
        at.concatenate(AffineTransform.getScaleInstance(1, -1));
        at.concatenate(AffineTransform.getTranslateInstance(0, -image.getHeight()));
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g = newImage.createGraphics();
	    g.transform(at);
	    g.drawImage(image, 0, 0, null);
	    g.dispose();
	    return newImage;
    }
	
	public static BufferedImage Vertical(BufferedImage image) {
		return createFlippedX(image);
	}
	
	public static BufferedImage Rotate(BufferedImage image, int degraus) {
		 BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		    Graphics2D g = newImage.createGraphics();
		    g.rotate(Math.toRadians(degraus), image.getWidth()/2, image.getHeight()/2);
		    g.drawImage(image, 0, 0, null);
		    g.dispose();
		    return newImage;
	}
	
	private static BufferedImage createFlippedX(BufferedImage image) {
        AffineTransform at = new AffineTransform();
        at.concatenate(AffineTransform.getScaleInstance(-1, 1));
        at.concatenate(AffineTransform.getTranslateInstance(-image.getWidth(), 0));
        BufferedImage newImage = new BufferedImage(
	    image.getWidth(), image.getHeight(),
	    BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g = newImage.createGraphics();
	    g.transform(at);
	    g.drawImage(image, 0, 0, null);
	    g.dispose();
	    return newImage;
    }
	
	public static BufferedImage Invert(BufferedImage image) {
		image = createFlippedX(image);
		image = createFlippedY(image);
		return image;
	}
	
}
