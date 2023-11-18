package com.coffee.io.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.coffee.io.Filer;

public class JSON_Reader extends Filer {

	private JSONObject Json;
	private JSONParser parse;
	private FileReader reader;
	
	public JSON_Reader(String fileName) {
		super(fileName);
		parse = new JSONParser();
		try {
			reader = new FileReader(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			Json = (JSONObject) parse.parse(reader);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
	
	public JSON_Reader(File filename) {
		super(filename);
		parse = new JSONParser();
		try {
			reader = new FileReader(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			Json = (JSONObject) parse.parse(reader);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
	
	public JSON_Reader(URL url) {
		super(url);
		InputStream istream = null;
		try {
			istream = url.openStream();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Reader isr = new InputStreamReader(istream);
		parse = new JSONParser();
		try {
			Json = (JSONObject) parse.parse(isr);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
	
	public int Length() {
		return Json.size();
	}
	
	public Object get(String key) {
		Object value = Json.get(key);
		if(value instanceof Long)
			return (int)(long)Json.get(key);
		if(value instanceof String)
			return (String)Json.get(key);
		if(value instanceof Boolean)
			return (boolean)Json.get(key);
		if(value instanceof Double)
			return (double)Json.get(key);
		if(value instanceof List)
			return (Object)Json.get(key);
		if(value instanceof JSONObject)
			return (JSONObject)Json.get(key);
		throw new RuntimeException("Value not exist");
	}
	
	public List<?> getList(String key) {
		Object value = Json.get(key);
		if(value instanceof List)
			return (List<?>)Json.get(key);
		throw new RuntimeException("Value not exist");
	}
	
}
