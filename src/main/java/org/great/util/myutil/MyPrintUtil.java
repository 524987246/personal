package org.great.util.myutil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

/**
 * 打印信息辅助类
 * 
 * @author xiejun
 * @date 2017-8-25 10:30:03
 * @since 1.0
 */
public class MyPrintUtil {
	public static void printMap(Map<String, Object> map) {
		Iterator<String> iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next().toString();
			print(key);
			print("==");
			print(map.get(key));
			println("");
		}
	}

	public static void printSet(Set<Object> set) {
		Iterator<Object> iterator = set.iterator();
		while (iterator.hasNext()) {
			String key = iterator.next().toString();
			println(key);
		}
	}

	public static void printList(List<Object> list) {
		for (Object object : list) {
			println(object);
		}
	}

	public static void println(Object object) {
		System.out.println(object.toString());
	}

	public static void print(Object object) {
		System.out.print(object.toString());
	}

	@Test
	public void mytest() {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("a", 1);
		tempMap.put("b", 2);
		tempMap.put("c", 3);
		// printMap(tempMap);
		Set<Object> set = new HashSet<Object>();
		set.add("1");
		set.add("9");
		set.add("6");
		printSet(set);
	}
}
