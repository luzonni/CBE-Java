package com.coffee.main;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.util.HashMap;
import java.util.Map;

import com.coffee.main.tools.Clips;

public class Sound {
	
	//Sound
	
	private static Map<String ,Clips> sounds;
	private static String[] names = {"poft", "click", "place", "clear", "kabum", "sss", "die"};
	
	public static void load() {
		sounds = new HashMap<String, Clips>();
		for(String name : names) {
			Clips clips = load(name.toLowerCase());
			sounds.put(name.toLowerCase(), clips);
		}	
	}
	
	public static void play(String name) {
		if(!sounds.containsKey(name))
			throw new RuntimeException("sound not exists");
		sounds.get(name).play();
	}
	
	public static void loop(String name) {
		if(!sounds.containsKey(name))
			throw new RuntimeException("sound not exists");
		sounds.get(name).loop();
	}
	
	public static void stop(String name) {
		if(!sounds.containsKey(name))
			throw new RuntimeException("sound not exists");
		sounds.get(name).stop();
	}
	
	private static Clips load(String name) {
		try {
			String path = Engine.ResPath+"/audio/" + name + ".wav";
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataInputStream dis = new DataInputStream(Sound.class.getResourceAsStream(path));
			byte[] buffer = new byte[4096];
			int read = 0;
			while((read = dis.read(buffer)) >= 0) {
				baos.write(buffer, 0, read);
			}
			baos.close();
			dis.close();
			byte[] data = baos.toByteArray();
			return new Clips(name, data, 64);
		}catch(Exception e) {
			System.out.println("Erro na criação do audio!");
			e.printStackTrace();
			try {
				return new Clips("", null, 0);
			}catch(Exception ee) {
				return null;
			}
		}
	}
	
}
