package com.iceps.spring.shardingjdbc2.utils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class StringUtils {

	public static String UUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	public static boolean isEmpty(String str) {
		return org.springframework.util.StringUtils.isEmpty(str);
	}

	/**
	 * Array的toString方法
	 * 
	 * @param array
	 * @return String
	 */
	public static String toString(Object[] array) {
		if (array == null)
			return null;
		String out = array.getClass().getSimpleName() + " [";
		for (int ii = 0; ii < array.length; ii++) {
			out += array[ii];
			if (ii + 1 < array.length)
				out += ",";
		}
		out += "]";
		return out;
	}

	/**
	 * Map的toString方法
	 * 
	 * @param map
	 * @return String
	 */
	public static String toString(Map<?, ?> map) {
		if (map == null)
			return null;
		String out = map.getClass().getSimpleName() + " {";
		Iterator<?> iter = map.keySet().iterator();
		while (iter.hasNext()) {
			Object key = iter.next();
			Object value = map.get(key);
			out += key + "=" + value;
			if (iter.hasNext())
				out += ",";
		}
		out += "}";
		return out;
	}

	/**
	 * List的toString方法
	 * 
	 * @param map
	 * @return String
	 */
	public static String toString(List list) {
		if (list == null)
			return null;
		String out = list.getClass().getSimpleName() + " [";
		for (int ii = 0; ii < list.size(); ii++) {
			out += list.get(ii);
			if (ii + 1 < list.size())
				out += ",";
		}
		out += "]";
		return out;
	}

	/**
	 * Set的toString方法
	 * 
	 * @param set
	 * @return String
	 */
	public static String toString(Set set) {
		if (set == null)
			return null;
		String out = set.getClass().getSimpleName() + " [";
		int ii = 0;
		for (Object o : set) {
			out += o;
			if (++ii < set.size())
				out += ",";
		}
		out += "]";
		return out;
	}

	/**
	 * Set的toString方法
	 * 
	 * @param set
	 * @return String
	 */
	public static String toString(Collection collect) {
		if (collect == null)
			return null;
		String out = collect.getClass().getSimpleName() + " [";
		int ii = 0;
		for (Object o : collect) {
			out += o;
			if (++ii < collect.size())
				out += ",";
		}
		out += "]";
		return out;
	}

	public static List toList(Object[] array) {
		if (array == null)
			return null;
		return Arrays.asList(array);
	}

	public static <T> T[] toArray(List<T> list, Class<? extends T> newType) {
		if (list == null || list.size() == 0)
			return null;
		return (T[]) list.toArray((T[]) Array.newInstance(newType, list.size()));
	}

}
