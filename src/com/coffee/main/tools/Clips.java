package com.coffee.main.tools;

import java.io.ByteArrayInputStream;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import com.coffee.main.Engine;

public class Clips {
	
	public Clip[] clips;
	private int p;
	private int count;
	
	private FloatControl[] gainControl;
	private final float MAX_VALUE = 6.0206f;
	private final float MIN_VALUE = -80f;
	private float volume = 0;
	private String name;
	
	public Clips(String name, byte[] buffer, int count) throws Exception {
		this.name = name;
		if(buffer == null) return;
		
		clips = new Clip[count];
		this.count = count;
		this.gainControl = new FloatControl[this.count];
		for(int i = 0; i < count; i++) {
			clips[i] = AudioSystem.getClip();
			clips[i].open(AudioSystem.getAudioInputStream(new ByteArrayInputStream(buffer)));
			gainControl[i] = (FloatControl) clips[i].getControl(FloatControl.Type.MASTER_GAIN);
		}
	}
	
	public void play() {
		if(clips == null) 
			return;
		setVolume(Engine.Volume);
		clips[p].stop();
		clips[p].setFramePosition(0);
		clips[p].start();
		p++;
		if(p >= count) 
			p = 0;
	}
	
	public void loop() {
		if(clips == null) 
			return;
		clips[p].loop(300);
	}
	
	public void stop() {
		clips[p].stop();
	}
	
	private void setVolume(int vol) {
		if(clips == null) 
			return;
		float def = MAX_VALUE - MIN_VALUE;
		float v = ((vol/100f)*def) + MIN_VALUE;
		if(volume != v) {
			volume = v;
			//O valor é de -80f até 6.0206f: o 0 é o valor/volume original;
			for(int i = 0; i < gainControl.length; i++) {
				gainControl[i].setValue(volume);
			}
		}
	}
	
	@Override
	public String toString() {
		return "Sound-"+name;
	}
	
}
