package com.coffee.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Random;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.coffee.graphics.FontG;
import com.coffee.main.activity.Activity;
import com.coffee.main.activity.Menu;
import com.coffee.main.ui.UserInterface;
import com.coffee.objects.Objects;

public class Engine implements Runnable {
	
	public static final String VERSION = "Version: 1.1.2 / Created by @lucaszonzini_";
	
	public static volatile Engine ME = null;
	
	//Teste
	
	public volatile Thread thread;
	public volatile boolean isRunning;

	public static final double HZ = 60;
	public static final double T = 1_000_000_000.0;
	public static int FRAMES;
	public static int HERTZ;
	
	public static boolean FullScreen = false;
	public static boolean AlwaysOnTop = false;
	public static int GameScale = 3;
	public static int Volume = 40;
	public static boolean ANTIALIASING = false;
	
	public static final int[][] resolutions = {{800, 600}, {1280, 720}, {1366, 768}, {1600, 900}, {1920, 1080}, {2560, 1440}, {3840, 2160}};
	public static int INDEX_RES = 1;

	public static final String ResPath = "/com/coffee/res";
	public static final String GameTag = "Code Break's Escape";
	public static double MaxFrames = 60;
	
	public static Window WINDOW;
	public static UserInterface UI;
	private static BufferedImage[] NOISE;
	private static int INDEX_NOISE;
	public static BufferStrategy Buffer;

	public static Activity ACTIVITY;
	public static boolean ACTIVITY_RUNNING;
	
	public static String[] LEVELS = {"start", "maybe"};
	public static int INDEX_LEVEL = 1;
	
	public static Random RAND;
	
	public static final int PRIMARY = 0xffffffff;
	public static final int SECUNDATY = 0xffcccccc;
	public static final int TERTIARY = 0xff000000;
	
	
	public final static Color[][] PALLET = {
				{new Color(180, 180, 180), new Color(80, 80, 80), new Color(0, 0, 0)},
				{new Color(233, 212, 165), new Color(127, 121, 99), new Color(26, 23, 18)},
				{new Color(248, 227, 86), new Color(180, 82, 48), new Color(54, 10, 61)},
				{new Color(117, 234, 241), new Color(58, 100, 150), new Color(40, 8, 75)},
				{new Color(149, 230, 75), new Color(50, 99, 116), new Color(32, 12, 47)},
				{new Color(226, 252, 165), new Color(52, 100, 80), new Color(6, 8, 16)},
				{new Color(249, 247, 196), new Color(120, 100, 125), new Color(6, 10, 48)},
				{new Color(141, 169, 144), new Color(94, 93, 91), new Color(27, 8, 27)},
				{new Color(40, 40, 160), new Color(50, 30, 100), new Color(0, 0, 0)}
			};
	
	public static volatile int INDEX_PALLET = 1;
	public static Color Color_Primary = new Color(233, 212, 165);
	public static Color Color_Secondary = new Color(127, 121, 99);
	public static Color Color_Tertiary = new Color(26, 23, 18);
	
	
	public static void main(String[] args) {
		FontG.addFont("septem");
		RAND = new Random();
		ME = new Engine();
		ME.start();
		Sound.load();
	}
	
	public Engine() {
		getConfig();
	}

	public synchronized void start() {
		SET_PALLET();
		WINDOW = new Window(GameTag + " / The Universe");
		UI = new UserInterface();
		ACTIVITY = new Menu();
		ACTIVITY.enter();
		ACTIVITY_RUNNING = true;
		thread = new Thread(ME, "Thread - Game");
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop() {
		thread.interrupt();
		new Thread(new Runnable() {
			@Override
			public void run() {
				WINDOW.getFrame().setVisible(false);
				WINDOW.getFrame().dispose();
				Objects.disposeAll();
				ME = new Engine();
				ME.start();
			}
		}).start();
		if(!WINDOW.isEnabled()) {
			WINDOW = null;
			UI = null;
		}
	}
	
	public synchronized static void restart() {
		ME.isRunning = false;
	}

	public static void SET_PALLET() {
		Color_Primary = PALLET[INDEX_PALLET][0];
		Color_Secondary = PALLET[INDEX_PALLET][1];
		Color_Tertiary = PALLET[INDEX_PALLET][2];
	}
	
	public static int[] getResolution() {
		return Engine.resolutions[Engine.INDEX_RES];
	}
	
	public synchronized static void setActivity(Activity activity) {
		ACTIVITY_RUNNING = false;
		Transition.start(() -> {
			ACTIVITY.dispose();
			ACTIVITY = activity;
			ACTIVITY.enter();
			ACTIVITY_RUNNING = true;
			UI.setReceiver(Engine.ACTIVITY);
		});
	}
	
	private void getConfig() {
		String path = System.getProperty("user.dir") + "/config.json";
		File file = new File(path);
		if(!file.exists()) 
			setConfig(Engine.Volume, Engine.INDEX_PALLET, Engine.FullScreen, Engine.INDEX_RES, Engine.GameScale);
		JSONObject object = null;
		try {
			InputStream istream = new FileInputStream(file);
			Reader isr = new InputStreamReader(istream);
			JSONParser parse = new JSONParser();
			parse.reset();
			object = (JSONObject) parse.parse(isr);
		}catch (Exception e) {}
		Engine.Volume = ((Number)object.get("VOLUM")).intValue();
		Engine.INDEX_PALLET = ((Number)object.get("PALLET")).intValue();
		Engine.FullScreen = ((Boolean)object.get("FULLSCREEN")).booleanValue();
		Engine.INDEX_RES = ((Number)object.get("RESOLUTION")).intValue();
		Engine.GameScale = ((Number)object.get("SCALE")).intValue();
	}
	
	public void setConfig(int volume, int pallet, boolean foolscreen, int res, int scale) {
		String path = System.getProperty("user.dir") + "/config.json";
		JSONObject object = new JSONObject();
		object.put("VOLUM", volume);
		object.put("PALLET", pallet);
		object.put("FULLSCREEN", foolscreen);
		object.put("RESOLUTION", res);
		object.put("SCALE", scale);
		try {
			FileWriter writer = new FileWriter(path);
			writer.write(object.toJSONString());
			writer.close();
		} catch (IOException e) {
			
		}
	}

	public static int getWidth() {
		return WINDOW.getWidth();
	}
	
	public static int getHeight() {
		return WINDOW.getHeight();
	}
	
	private static BufferedImage getNoise() {
		if(NOISE == null)
			NOISE = new BufferedImage[100];
		INDEX_NOISE++;
		if(INDEX_NOISE > NOISE.length-1)
			INDEX_NOISE = 0;
		if(NOISE[INDEX_NOISE] == null || NOISE[INDEX_NOISE].getWidth() != WINDOW.getWidth()/GameScale)
			NOISE[INDEX_NOISE] = buildNoise();
		return NOISE[INDEX_NOISE];
	}
	
	private static BufferedImage buildNoise() {
		int width = WINDOW.getWidth()/GameScale;
		int height = WINDOW.getHeight()/GameScale;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		int[] rgb = new int[width*height];
		for(int y = 0; y < height; y++)
			for(int x = 0; x < width; x++) {
				rgb[x+y*width] = new Color(Color_Primary.getRed(), Color_Primary.getGreen(), Color_Primary.getBlue(), RAND.nextInt(35)).getRGB();
			}
		image.setRGB(0, 0, width, height, rgb, 0, width);	
		return image;
	}
	
	private Graphics2D getGraphics() {
		Graphics2D graphics = (Graphics2D) Buffer.getDrawGraphics();
		if(ANTIALIASING)
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setColor(Color_Tertiary);
		graphics.fillRect(0, 0, WINDOW.getWidth(), WINDOW.getHeight());
		return graphics;
	}
	
	private void render(Graphics2D graphics) {
		graphics.drawImage(Engine.getNoise(), 0, 0, Engine.WINDOW.getWidth(), Engine.WINDOW.getHeight(), null);
		graphics.dispose();
		Buffer.show();
	}
	
	@Override
	public void run() {
		//System values
		long lastTimeHZ = System.nanoTime();
		double amountOfHz = Engine.HZ;
		double ns_HZ = Engine.T / amountOfHz;
		double delta_HZ = 0;
		
		long lastTimeFPS = System.nanoTime();
		double amountOfFPS = Engine.MaxFrames;
		double ns_FPS = Engine.T / amountOfFPS;
		double delta_FPS = 0;
		
		//To Show
		int Hz = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		while(isRunning) {
			try {
				long nowHZ = System.nanoTime();
				delta_HZ += (nowHZ - lastTimeHZ) / ns_HZ;
				lastTimeHZ = nowHZ;
				if(delta_HZ >= 1) {
					if(ACTIVITY_RUNNING && ACTIVITY != null) {
						ACTIVITY.tick();
						UI.tick();
					}
					Hz++;
					delta_HZ--;
				}
				
				long nowFPS = System.nanoTime();
				delta_FPS += (nowFPS - lastTimeFPS) / ns_FPS;
				lastTimeFPS = nowFPS;
				if(delta_FPS >= 1) {
					Graphics2D g = getGraphics();
					if(ACTIVITY_RUNNING && ACTIVITY != null) {
						ACTIVITY.render(g);
						UI.render(g);
						render(g);
					}
					frames++;
					delta_FPS--;
				}
				
				//Show fps
				if(System.currentTimeMillis() - timer >= 1000){
					Engine.WINDOW.getFrame().setTitle(Engine.GameTag+" - Hz: " + Hz + " / Frames: " + frames);
					Engine.FRAMES = frames;
					frames = 0;
					Engine.HERTZ = Hz;
					Hz = 0;
					timer+=1000;
				}
				
			}catch(Exception e) {
				System.out.println("ERROR!");
				e.printStackTrace();
				System.exit(1);
			}
		}
		stop();
	}

}

/*

Calcular tempo de execuï¿½ï¿½o {
	long startTime = System.nanoTime();
	
	...
	
	long estimatedTime = System.nanoTime() - startTime;
	System.out.printf("Draw runtime: %.4f ms%n", estimatedTime / 1000000d);
}


*/
