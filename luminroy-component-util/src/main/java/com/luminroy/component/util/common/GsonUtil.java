package com.luminroy.component.util.common;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class GsonUtil {

	private final static String DATA_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 创建gson
	 * 
	 * @return
	 */
	public static Gson createGson() {
		return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat(DATA_FORMAT).create();
	}

	/**
	 * json字符串转成对象
	 * 
	 * @param str
	 * @param type
	 * @return
	 */
	public static <T> T fromJson(String str, Type type) {
		Gson gson = createGson();
		return gson.fromJson(str, type);
	}

	/**
	 * json字符串转成对象
	 * 
	 * @param str
	 * @param type
	 * @return
	 */
	public static <T> ArrayList<T> fromJsonList(String json, Class<T> cls) {
		Gson gson = createGson();
		ArrayList<T> mList = new ArrayList<T>();
		JsonArray array = new JsonParser().parse(json).getAsJsonArray();
		for (final JsonElement elem : array) {
			mList.add(gson.fromJson(elem, cls));
		}
		return mList;
	}
	
}
