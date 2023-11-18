package com.coffee.graphics;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.io.InputStream;

public class FontG {
	

	private static InputStream stream;
	private static Font font;
	
	public static void addFont(String name) {
		try {
			stream = ClassLoader.getSystemClassLoader().getResourceAsStream("com/coffee/res/ui/" + name + ".ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, stream);
		} catch (FontFormatException | IOException e) {
			System.out.println("ERROR!");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public static Font font(float size) {
		return font.deriveFont(size);
	}
	
	public static int getWidth(String text, Font font) {
		FontRenderContext frc = new FontRenderContext(new AffineTransform(), false, false);
		return (int)(font.getStringBounds(text, frc).getWidth());
	}
	
	public static int getHeight(String text, Font font) {
		FontRenderContext frc = new FontRenderContext(new AffineTransform(), false, false);
		return (int)(font.getStringBounds(text, frc).getHeight() - 0.29*font.getSize());
	}
	
	public static int getSize() {
		return font.getSize();
	}
	
}
