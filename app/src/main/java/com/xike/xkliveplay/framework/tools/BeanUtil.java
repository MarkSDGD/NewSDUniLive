package com.xike.xkliveplay.framework.tools;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BeanUtil {

	/**
	 * 存放字段对应的Field、get方法、set方法
	 */
	public static class BeansInfo {
		String name;
		Field field;
		Method readMethod;
		Method writeMethod;

		public void setValue(Object obj, Object value) {
			try {
				writeMethod.invoke(obj, value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public Object getValue(Object obj) {
			Object value = null;
			try {
				value = readMethod.invoke(obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return value;
		}
	}



	public static Map<String, BeansInfo> getBeanInfo(Class<?> clazz)
			throws Exception {
		Map<String, BeansInfo> _cache = new HashMap<String, BeansInfo>();
		Field[] fileds = clazz.getDeclaredFields();

		for (Field field : fileds) {
			Class<?> type = field.getType();
			String name = FirstChartoUpperCase(field.getName());
			BeansInfo info = new BeansInfo();
			Method readMethod = null;// get方法
			Method writeMethod = null;// set方法

			try {
				readMethod = clazz.getMethod("get" + name);// get方法
				info.readMethod = readMethod;
			} catch (Exception e) {
			}
			try {
				writeMethod = clazz.getMethod("set" + name, type);// set方法
				info.writeMethod = writeMethod;
			} catch (Exception e) {
			}

			if (readMethod != null && writeMethod != null) {
				info.name = field.getName();
				info.field = field;
				_cache.put(field.getName().toUpperCase(), info);
			}
		}

		return _cache;
	}

	public static String FirstChartoUpperCase(String str) {
		if (str == null || "".equals(str))
			return null;
		String str1 = str.substring(0, 1).toUpperCase();
		String str2 = str.substring(1, str.length());
		return str1 + str2;
	}

}
