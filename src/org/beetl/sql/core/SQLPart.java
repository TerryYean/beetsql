package org.beetl.sql.core;

import java.util.Map;

/**
 * 用于弥补基于模板更新不足，用来添加基于额外的更新和插入值，如一对一关系
 * 
 * @author xiandafu
 *
 */
public class SQLPart {
	
	public Map getUpdateValue(String colName,Object value){
		return null;
	}
	
	public String getSelectSuffix(){
		return null;
	}
}
