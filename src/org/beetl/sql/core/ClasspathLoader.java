package org.beetl.sql.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.beetl.sql.SQLLoader;
import org.beetl.sql.annotation.ID;

/**
 * 从classpath系统加载sql模板，id应该格式是"xx.yyy",xx代表了文件名，yyy代表了sql标识 sql 模板格式如下：
 * 
 * ==selectUser * comment select * from user where .. * ==selectAgenyUser select
 * * from agencyUser where .. *
 * 
 * 
 * @author Administrator
 *
 */
public class ClasspathLoader implements SQLLoader {

	String sqlRoot = null;

	public static Map<String, SQLSource> sqlSourceMap = new HashMap<String, SQLSource>();

	public ClasspathLoader(String sqlRoot) {
		this.sqlRoot = sqlRoot;
	}

	@Override
	public SQLSource getSQL(String id) {
		// real path = sqlRoot\xx\yy.sql
		SQLSource ss = this.sqlSourceMap.get(id);
		if (ss == null) {
			loadSql(id);
		}
		ss = this.sqlSourceMap.get(id);
		return ss;
	}
	final String SYMBOL_BEGIN = "#";
	final String SYMBOL_END = "\n";
	/***
	 * 生成getbyid语句
	 */
	@Override
	public SQLSource generationGetByid(Class cls) {
		String className = cls.getSimpleName().toLowerCase();
		SQLSource tempSource = this.sqlSourceMap.get(className + ".getById");
		if (tempSource != null) {
			return tempSource;
		}
		Method[] methods = cls.getDeclaredMethods();
		String condition = null;
		for (Method method : methods) {
			if (method.isAnnotationPresent(ID.class)) {
				String fieldName = method.getName().substring(3).toLowerCase();
				condition = " where " + fieldName + "=${" + className+ "." + fieldName + "}";
			}
		}
		if (condition == null) {
			condition = " where id=${" + className + ".id}";
		}
		String sql = "select * from " + className + condition;
		tempSource = new SQLSource(sql);
		this.sqlSourceMap.put(className + ".getById", tempSource);
		return tempSource;
	}

	/****
	 * 自动生成update语句
	 */
	@Override
	public SQLSource generationUpdate(Class cls) {
		String className = cls.getSimpleName().toLowerCase();
		SQLSource tempSource = this.sqlSourceMap.get(className+".update");
		if(tempSource != null){
			return tempSource;
		}
		String sql = "update " + className + " set \n";
		String clsField = null;
		String fieldName = null;
		Method[] methods = cls.getDeclaredMethods();
		String condition = null;
		for (Method method : methods) {
			if(method.getName().startsWith("get")){
				fieldName = method.getName().substring(3).toLowerCase();
				clsField = className+"."+fieldName;
				sql = sql+SYMBOL_BEGIN+"if(!isEmpty("+clsField+")){"+SYMBOL_END+fieldName+"='${"+clsField+"}',\n"+SYMBOL_BEGIN+"}"+SYMBOL_END;
				if (method.isAnnotationPresent(ID.class)) {
					condition = " where " + fieldName + "=${" +clsField+ "}";
				}
			}
		}
		if(condition == null){
			 condition = " where id=${"+className+".id}";
		}
		sql = sql.subSequence(0, sql.lastIndexOf(","))+"\n"+SYMBOL_BEGIN+"}\n "+condition;
		tempSource = new SQLSource(sql);
		this.sqlSourceMap.put(className+".update",tempSource);
		return tempSource;
	}

	/***
	 * 加载sql文件，并放入sqlSourceMap中
	 * 
	 * @param file
	 * @return
	 */
	private boolean loadSql(String id) {
		String modelName = id.substring(0, id.lastIndexOf(".") + 1);
		InputStream ins = this.getClass().getResourceAsStream(
				sqlRoot + File.separator + modelName + "md");
		LinkedList<String> list = new LinkedList<String>();
		BufferedReader bf = null;
		try {
			bf = new BufferedReader(new InputStreamReader(ins));
			String temp = null;
			StringBuffer sql = null;
			String key = null;
			while ((temp = bf.readLine()) != null) {
				if (temp.startsWith("===")) {// 读取到===号，说明上一行是key，下面是SQL语句
					if (!list.isEmpty() && list.size() > 1) {// 如果链表里面有多个，说明是上一句的sql+下一句的key
						String tempKey = list.pollLast();// 取出下一句sql的key先存着
						sql = new StringBuffer();
						key = list.pollFirst();
						while (!list.isEmpty()) {// 拼装成一句sql
							sql.append(list.pollFirst() + "\n");
						}
						this.sqlSourceMap.put(modelName + key, new SQLSource(
								sql.toString()));// 放入map
						list.addLast(tempKey);// 把下一句的key又放进来
					}
				} else {
					list.addLast(temp);
				}
			}
			// 最后一句sql
			sql = new StringBuffer();
			key = list.pollFirst();
			while (!list.isEmpty()) {
				sql.append(list.pollFirst());
			}
			this.sqlSourceMap.put(modelName + key,
					new SQLSource(sql.toString()));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bf != null) {
				try {
					bf.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return true;
	}

}
