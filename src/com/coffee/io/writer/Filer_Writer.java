package com.coffee.io.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.coffee.io.Filer;
import com.coffee.io.reader.Filer_Reader;

public class Filer_Writer extends Filer {


	private FileWriter filer;
	private BufferedWriter write;
	
	public Filer_Writer(String fileName) {
		super(fileName);
		createFile();
	}
	
	public Filer_Writer(File fileName) {
		super(fileName);
		createFile();
	}
	
	private void createFile() {
		try {
			filer = new FileWriter(file.getPath());
			write = new BufferedWriter(filer);
		}catch(IOException e) {}
	}
	
	public void put(Object... content) {
		StringBuffer current = new StringBuffer();
		for(int i = 0; i < content.length; i++) {
			if(content[i] == null)
				current.append("null@N");
			else {
				current.append(content[i].toString());
				current.append("@"+content[i].getClass().getSimpleName().toCharArray()[0]);
			}
			if(i < content.length-1) current.append(":");
		}
		current.append("/");
		current = Cryptography(current);
		try {
			write.write(current.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void append(File file, Object... content) {
		StringBuffer current = new StringBuffer();
		BufferedWriter w;
		Filer f = new Filer(file);
		try {
			w = new BufferedWriter(new FileWriter(file.getPath(), true));
		}catch(IOException e) {
			return;
		}
		for(int i = 0; i < content.length; i++) {
			if(content[i] == null)
				current.append("null@N");
			else {
				current.append(content[i].toString());
				current.append("@"+content[i].getClass().getSimpleName().toCharArray()[0]);
			}
			if(i < content.length-1) current.append(":");
		}
		current.append("/");
		current = f.Cryptography(current);
		try {
			for(int i = 0; i < current.length(); i++) {
				w.append(current.charAt(i));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			w.flush();
			w.close();
		}catch(IOException e) {}
	}
	
	public static void deletContent(File file, String value, Class<?> type) {
		File newFile = new File(file.getAbsolutePath());
		Filer_Reader reader = new Filer_Reader(file);
		String content = value+"@"+type.getSimpleName().toCharArray()[0];
		String[] contents = reader.getContent().split("/");
		for(int i = 0; i < contents.length; i++) {
			if(contents[i].equals(content))
				contents[i] = null;
		}
		try {
			BufferedWriter w = new BufferedWriter(new FileWriter(newFile.getPath()));
			for(String c : contents) {
				if(c == null)
					continue;
				w.write(c+"/");
			}
			w.flush();
			w.close();
		}catch(IOException e) {
			return;
		}
	}
	
	public void closeFile() {
		try {
			write.flush();
			write.close();
		}catch(IOException e) {e.printStackTrace();}
	}
	
}
