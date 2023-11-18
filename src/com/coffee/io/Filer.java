package com.coffee.io;

import java.io.File;
import java.net.URL;

public class Filer {
	
	public File file;
	public URL url;
	public boolean Cryptography;
	
	public static final char[][] crying = {
			{';','['},{':','{'},{'/', '('},
			
			{'1','?'},{'2','#'},{'3','@'},{'4','*'},{'5','&'},{'6','%'},{'7','+'},{'8','-'},{'9','_'},{'0','!'},
			
			{'A','y'},{'B','l'},{'C','a'},{'D','i'},{'E','t'},{'F','v'},{'G','s'},{'H','d'},{'I','n'},{'J','k'},
			{'K','b'},{'L','m'},{'M','g'},{'N','h'},{'O','j'},{'P','x'},{'Q','o'},{'R','p'},{'S','e'},{'T','c'},
			{'U','w'},{'V','q'},{'W','r'},{'X','f'},{'Y','z'},{'Z','u'}
	};
	
	public StringBuffer Cryptography(StringBuffer tocry) {
		if(!Cryptography)
			return tocry;
		StringBuffer newString = new StringBuffer();
		char[] c = tocry.toString().toCharArray();
		for(int i = 0; i < c.length; i++) {
			for(int index = 0; index < crying.length; index++) {
				if(c[i] == crying[index][0]) {
					c[i] = crying[index][1];
				}else if(c[i] == crying[index][1]) {
					c[i] = crying[index][0];
				}
			}
			newString.append(c[i]);
		}
		return newString;
	}
	
	public static File[] getFiles(String directory) {
		File[] files = new File(directory).listFiles();
		return files;
	}
	
	public static void changeName(File file, String newName) {
		file.renameTo(new File(file.getParentFile()+"/"+newName));
	}
	
	public Filer(String fileName) {
		file = new File(fileName);
	}
	
	public Filer(File fileName) {
		file = fileName;
	}
	
	public Filer(URL url) {
		this.url = url;
	}
	
	public Filer setCryptography() {
		Cryptography = true;
		return this;
	}
	
}
