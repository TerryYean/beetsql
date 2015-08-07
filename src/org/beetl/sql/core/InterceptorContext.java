package org.beetl.sql.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterceptorContext {
	private String sqlId;
	private String sql;
	private  List<Object> paras;
	private Map<String,Object> env  = null;
	public InterceptorContext(String sqlId,String sql,List<Object> paras){
		this.sql = sql ;
		this.paras = paras;
		this.sqlId = sqlId;
	}
	public void put(String key,Object value){
		if(env==null){
			env = new HashMap<String,Object>();
		}
		env.put(key, value);
	}
	
	public Object get(String key){
		if(env==null){
			return null;
		}else{
			return env.get(key);
		}
		
	}
	public String getSql() {
		return sql;
	}
	
	public List<Object> getParas() {
		return paras;
	}
	public String getSqlId() {
		return sqlId;
	}
	
	
	
}
