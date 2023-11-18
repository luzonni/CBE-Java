package com.coffee.io.writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.coffee.io.Filer;

public class JSON_Writer extends Filer {

	private FileWriter writer;
	private JSONObject Json;
	
	public JSON_Writer(File fileName) {
		super(fileName);
		try {
			writer = new FileWriter(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Json = new JSONObject();
	}
	
	public JSON_Writer(String fileName) {
		super(fileName);
		try {
			writer = new FileWriter(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Json = new JSONObject();
	}
	
	public void put(Object key, Object value) {
		if(value instanceof List) {
			JSONArray array = new JSONArray();
			List<Object> list = (List<Object>)value;
			for(Object o : list)
				array.add(o);
			value = array;
		}
		if(value instanceof Object[]) {
			JSONArray array = new JSONArray();
			Object[] list = (Object[])value;
			for(Object o : list)
				array.add(o);
			value = array;
		}
		Json.put(key, value);
	}
	
	public void save() {
		try {
			writer.write(Json.toJSONString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
