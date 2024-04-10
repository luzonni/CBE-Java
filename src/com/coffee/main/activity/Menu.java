package com.coffee.main.activity;

import java.awt.AlphaComposite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.coffee.Inputs.Mouse;
import com.coffee.Inputs.Mouse_Button;
import com.coffee.Inputs.Button.Button;
import com.coffee.command.Receiver;
import com.coffee.graphics.FontG;
import com.coffee.graphics.SpriteSheet;
import com.coffee.main.Engine;
import com.coffee.main.Geometry;
import com.coffee.main.tools.Responsive;
import com.coffee.objects.tiles.Tile;

public class Menu implements Activity, Receiver {

	private Button C, P, Q, O;
	
	private BufferedImage Logo;
	private Responsive logo_res;
	private BufferedImage background;
	
	private static List<Buoyant> list;
	
	public Menu() {
		Responsive center = Responsive.createPoint(null, 50, 10);
		C = new Button("Creator", -5, 0, center, 10);
		P = new Button("Play", -10, 0, C.getResponsive(), 10);
		O = new Button("Options", 5, 0, center, 10);
		Q = new Button("Quit", 10, 0, O.getResponsive(), 8);
		if(list == null)
			list = new ArrayList<Buoyant>();
		SpriteSheet sp = new SpriteSheet(Engine.ResPath + "/ui/LogoName.png", 3*Engine.GameScale);
		sp.replaceColor(0xffffffff, Engine.Color_Primary.getRGB());
		sp.replaceColor(0xff000000, Engine.Color_Tertiary.getRGB());
		Logo = sp.getImage();
		logo_res = Responsive.createRectangle(null, new Rectangle(Logo.getWidth(), Logo.getHeight()), 50, 50);
	}
	
	@Override
	public void enter() {
//		if(list.isEmpty()) {
//			List<Objects> toDispose = new ArrayList<Objects>();
//			int amount = Engine.RAND.nextInt(45) + 50;
//			for(int i = 0; i < amount; i++) {
//				List<BufferedImage> list_sprites = new ArrayList<BufferedImage>();
//				int index = 1;
//				long startTime = System.nanoTime();
//				while(true) {
//					try {
//						Tile tile = null;
//						for(Objects o : toDispose) {
//							if(o instanceof Tile && o.ID() == index) {
//								tile = (Tile)o;
//							}
//						}
//						if(tile == null)
//							tile = Tile.Factory(index, 0, 0);
//						list_sprites.add(tile.getSprite());
//						tile.dispose();
//					}catch(RuntimeException e) {
//						break;
//					}
//					index++;
//				}
//				long estimatedTime = System.nanoTime() - startTime;
//				System.out.printf("Draw runtime: %.4f ms%n", estimatedTime / 1000000d);
//				index = 1;
//				while(true) {
//					try {
//						Entity entity = null;
//						for(Objects o : toDispose) {
//							if(o instanceof Entity && o.ID() == index) {
//								entity = (Entity)o;
//							}
//						}
//						if(entity == null)
//							entity = Entity.Factory(index, 0, 0);
//						list_sprites.add(entity.getSprite());
//						entity.dispose();
//					}catch(RuntimeException e) {
//						break;
//					}
//					index++;
//				}
//				int x = Engine.RAND.nextInt(Engine.getWidth() - Tile.getSize());
//				int y = Engine.RAND.nextInt(Engine.getHeight() - Tile.getSize());
//				list.add(new Buoyant(list_sprites.get(Engine.RAND.nextInt(list_sprites.size())), x, y, Tile.getSize(), Tile.getSize()));
//			}
//			for(Objects o : toDispose) {
//				o.dispose();
//			}
//		}
	}
	
	private void drawBackground() {
		background = new BufferedImage(Engine.getWidth(), Engine.getHeight(), BufferedImage.TYPE_INT_ARGB);
		SpriteSheet sp = new SpriteSheet(Engine.ResPath+"/ui/background.png", 2*Engine.GameScale);
		sp.replaceColor(0xffffffff, Engine.Color_Secondary.getRGB());
		sp.replaceColor(0xff000000, Engine.Color_Tertiary.getRGB());
		Graphics2D g = (Graphics2D)background.getGraphics();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
		int w = Engine.getWidth() / sp.getWidth();
		int h = Engine.getHeight() / sp.getHeight();
		for(int y = 0; y < h; y++) {
			for(int x = 0; x < w; x++) {
				g.drawImage(sp.getImage(), x*sp.getImage().getWidth(), y*sp.getImage().getHeight(), null);
			}
		}
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		g.dispose();
	}
	
	public void tick() {
		logo_res.setPosition();
		if(background == null || (background.getWidth() != Engine.getWidth() || background.getHeight() != Engine.getHeight()))
			drawBackground();
		for(int i = 0; i < list.size(); i++) {
			list.get(i).tick();
		}
		if(Mouse.click(Mouse_Button.LEFT)) {
			int x = Mouse.getX();
			int y = Mouse.getY();
			for(int i = 0; i < list.size(); i++) {
				Buoyant b = list.get(i);
				if(Geometry.Theta(x, y, b.middle().x, b.middle().y) < 64*Engine.GameScale) {
					double a = Math.atan2(b.middle().y - y, b.middle().x - x);
					b.direction = a;
					b.speed += 15;
				}
			}
		}
		if(P.function()) {
			Engine.setActivity(new LevelMap());
		}
		if(C.function())
			Engine.setActivity(new Creator(null));
		if(O.function())
			Engine.setActivity(new Options());
		if(Q.function()) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) { }
			System.exit(0);
		}
	}
	
	public void render(Graphics2D g) {
		g.drawImage(background, 0, 0, null);
		for(int i = 0; i < list.size(); i++) {
			list.get(i).render(g);
		}
		P.render(g);
		C.render(g);
		Q.render(g);
		O.render(g);
		Rectangle rec = logo_res.getBounds();
		g.drawImage(Logo, rec.x, rec.y, null);
		Font f = FontG.font(8*Engine.GameScale);
		String value = "Version: 1.1.2 / Created by @lucaszonzini_";
		int wF = FontG.getWidth(value, f);
		int hF = FontG.getHeight(value, f);
		int x = Engine.getWidth() - wF - Engine.GameScale;
		int y = Engine.getHeight() - hF/2;
		g.setColor(Engine.Color_Primary);
		g.setFont(f);
		g.drawString(value, x, y);
	}

	@Override
	public String giveCommand(String[] values) {
		String message = "";
		String text = "";
		for(String v : values)
			text += v+" ";
		text = text.trim();
		message += response(text);
		return message.trim();
		
	}

	private volatile int index_how_to_play = 0;
	private String response(String message) {
		if(message.equalsIgnoreCase("hello"))
			return "Hello! How are you?";
		if(message.equalsIgnoreCase("im good"))
			return "That's good, soon it won't be anymore!";
		if(message.equalsIgnoreCase("How do i play this")) {
			String[] steps = {
					"At each voice level you will have to use strategies",
					"Along with commands that the level made available",
					"You can use WASD keys to walk",
					"Or the arrows"
			};
			index_how_to_play = 0;
			new Thread(() -> {
				while(index_how_to_play < steps.length) {
					if(!Engine.UI.getConsole().isWriting()) {
						Engine.UI.getConsole().print(steps[index_how_to_play], true);
						index_how_to_play++;
					}
				}
			}).start();
			return "";
		}
		if(message.equalsIgnoreCase("how do i play the level i created")) {
			String[] steps = {
					"After saving the level",
					"It will be saved in the same folder where the game was executedes",
					"Press \"Play\" and type the command:",
					"load (level name)",
					"Remembering that the level needs to be in the same directory as me"
			};
			index_how_to_play = 0;
			new Thread(() -> {
				while(index_how_to_play < steps.length) {
					if(!Engine.UI.getConsole().isWriting()) {
						Engine.UI.getConsole().print(steps[index_how_to_play], true);
						index_how_to_play++;
					}
				}
			}).start();
			return "";
		}
		if(message.equalsIgnoreCase("Oi"))
			return "Oi oque? vai jogar logo oxe";
		else
			return "I didn't understand what you meant";
	}

	public void dispose() {
		list.clear();
	}
	
	private static class Buoyant {
		
		private BufferedImage sprite;
		private double x, y;
		private int width, height;
		private double direction;
		private int speed;
		private int default_speed;
		private double rotation;
		private int timer;
		
		public Buoyant(BufferedImage sprite, double x, double y, int width, int height) {
			this.sprite = sprite;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.direction = Engine.RAND.nextDouble() * (Math.PI*2);
			this.speed = Engine.RAND.nextInt(2) + 1;
			this.default_speed = speed;
		}
		
		public void tick() {
			if(this.x > Engine.getWidth() - Tile.getSize() || this.x < 0) {
				double defx = Math.cos(direction);
				double defy = Math.sin(direction);
				defx *= -1;
				this.direction = Math.atan2(defy, defx);
			}
			if(this.y > Engine.getHeight() - Tile.getSize() || this.y < 0) {
				double defx = Math.cos(direction);
				double defy = Math.sin(direction);
				defy *= -1;
				this.direction = Math.atan2(defy, defx);
			}
			this.x += Math.cos(direction) * speed;
			this.y += Math.sin(direction) * speed;
			this.rotation += (double)this.speed / 100d;
			this.timer++;
			if(timer > 5 && this.speed > this.default_speed) {
				this.timer = 0;
				this.speed -= 0.15;
			}
		}
		
		public Point middle() {
			return new Point((int)x + width/2, (int)y + height/2);
		}
		
		public void render(Graphics2D g) {
			g.rotate(rotation, x + width/2, y + height/2);
			g.drawImage(sprite, (int)x, (int)y, width, height, null);
			g.rotate(-rotation, x + width/2, y + height/2);
		}
		
	}
	
	
}
