package com.coffee.io.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import com.coffee.io.Filer;

public class Filer_Reader extends Filer {

	private Reader reader;
	private BufferedReader bufferedReader;
	
	private String Content;
	private String[] Containers;
	
	public Filer_Reader(String fileName) {
		super(fileName);
		createReader();
	}
	
	public Filer_Reader(File fileName) {
		super(fileName);
		createReader();
	}
	
	public Filer_Reader(URL url) {
		super(url);
		createReader();
	}
	
	private void createReader() {
		if(file == null)
			try {
				reader = new InputStreamReader(url.openStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		else {
			if(!file.exists())
				throw new RuntimeException("File name:"+file.getAbsolutePath()+" not found!");
			try {
				reader = new InputStreamReader(new FileInputStream(file));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.Content = loadFileContainer();
		Containers = getContainers();
	}
	
	public String getContent() {
		return Content;
	}
	
	private String loadFileContainer() {
		StringBuffer line = new StringBuffer();
		String singleLine = null;
		bufferedReader = new BufferedReader(reader);
		try {
			while((singleLine = bufferedReader.readLine()) != null) {
				singleLine = Cryptography(new StringBuffer(singleLine)).toString();
				line.append(singleLine);
			}
		}catch(IOException e) { }
		return line.toString();
	}

	private String[] getContainers() {
		if(this.Content.equals(""))
			return null;
		return this.Content.split("/");
	}
	
	public Object[] getValues(int indexContainer) {
		int length = Containers[indexContainer].split(":").length;
		if(Containers == null)
			return null;
		String[] cantents = Containers[indexContainer].split(":");
		Object[] values = new Object[length];
		for(int i = 0; i < length; i++) {
			try {
				values[i] = toObject(cantents[i]);
			}catch(NumberFormatException e) {
				
			}
		}
		return values;
	}
	
	private Object toObject(String values) {
		String val = values.split("@")[0];
		String type = values.split("@")[1];
		switch(type) {
		case "S" -> {return val;}
		case "D" -> {return Double.parseDouble(val);}
		case "B" -> {return Boolean.parseBoolean(val);}
		case "I" -> {return Integer.parseInt(val);}
		case "F" -> {return Float.parseFloat(val);}
		case "L" -> {return Long.parseLong(val);}
		case "N" -> {return null;}
		}
		throw new RuntimeException("Value not found");
	}
	
	public int Length() {
		if(Containers == null)
			return 0;
		return Containers.length;
	}
	
}
