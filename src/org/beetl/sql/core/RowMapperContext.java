package org.beetl.sql.core;

import java.util.HashMap;
import java.util.Map;

public class RowMapperContext {
	SQLManager sm;
	SQLScript script;
	Map<Class,Map<Object,Object>> cache = new HashMap<Class,Map<Object,Object>>();
	
	public SQLManager getSQLManager() {
		return sm;
	}

	public SQLScript getSQLScript() {
		return script;
	}

	public Object getCache(Class c,Object key) {
		Map<Object,Object> map = cache.get(c);
		if(map==null){
			return null;
		}
		Object value = map.get(key);
		return value ;
	}
	public void setCache(Class c,Object key,Object value) {
		Map<Object,Object> map = cache.get(c);
		if(map==null){
			map = new HashMap<Object,Object>();
			cache.put(c, map);
		}
		map.put(key, value);
		
	}
	
	
}
