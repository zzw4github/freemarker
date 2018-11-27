package me.zzw.study.freemarker.start;

import java.util.HashMap;
import java.util.Map;

public class MyDataModel {
	@SuppressWarnings("rawtypes")
	public static Map getData() {
	// Create the root hash. We use a Map here, but it could be a JavaBean too.
	Map<String, Object> root = new HashMap<>();

	// Put string "user" into the root
	root.put("user", "Big Joe");

	// Create the "latestProduct" hash. We use a JavaBean here, but it could be a Map too.
	Product latest = new Product();
	latest.setUrl("products/greenmouse.html");
	latest.setName("green mouse");
	// and put it into the root
	root.put("latestProduct", latest); 
	return root;
	}
}
