package org.beetl.sql.core;

import org.beetl.sql.SQLLoader;

/**
 * 从classpath系统加载sql模板，id应该格式是"xx.yyy",xx代表了文件名，yyy代表了sql标识
 * sql 模板格式如下：
 * 
 * ==selectUser 
 * * comment
 * select * from user where .. * 
 * ==selectAgenyUser
 * select * from agencyUser where .. * 
 * 
 * 
 * @author Administrator
 *
 */
public class ClasspathLoader implements SQLLoader {

	String sqlRoot = null;
	public ClasspathLoader(String sqlRoot){
		
	}
	@Override
	public SQLSource getSQL(String id) {
		//real path = sqlRoot\xx\yy.sql
		return null;
	}

}
