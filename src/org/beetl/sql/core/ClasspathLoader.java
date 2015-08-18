package org.beetl.sql.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.beetl.sql.core.db.DBStyle;
import org.beetl.sql.core.db.MySqlStyle;

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

	private String lineSeparator = System.getProperty("line.separator", "\n");

	private static Map<String, SQLSource> sqlSourceMap = new ConcurrentHashMap<String, SQLSource>();
	private static Map<String, Long> sqlSourceVersion = new ConcurrentHashMap<String, Long> ();
	

//	private DBStyle dbs = null;
	
	private boolean autoCheck = true;

	public  ClasspathLoader(String root) {
		this.sqlRoot = root;
	}

	@Override
	public SQLSource getSQL(String id) {
		SQLSource ss = sqlSourceMap.get(id);		
		if (ss == null) {
			loadSql(id);
		}
		if(this.autoCheck&&isModified(id)){
			loadSql(id);
		}
		ss = sqlSourceMap.get(id);
		return ss;
		
	
	}
	
	@Override
	public boolean isModified(String id) {
		File file = this.getFile(id);	//TODO 可能会有问题：感觉应该是处理id，id可能是user.selectUser ，应该处理为user.md
		if(file==null) return true;
		long lastModify = file.lastModified();
		Long oldVersion = sqlSourceVersion.get(id);
		if(oldVersion==null) return true;
		if(oldVersion!=lastModify){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean exist(String id){
		return loadSql(id);
	}
	
	@Override
	public void addSQL(String id, SQLSource source) {
		sqlSourceVersion.put(id, 0l); //never change
		sqlSourceMap.put(id, source);
		
	}

	/***
	 * 加载sql文件，并放入sqlSourceMap中
	 * 
	 * @param file
	 * @return
	 */
	private boolean loadSql(String id) {
		
		String modelName = id.substring(0, id.lastIndexOf(".") + 1);
		File file = this.getFile(id);
		if(file==null) return false ;
		InputStream ins  = null;
		try{
			ins = new FileInputStream(file);
		}catch(IOException ioe){
			throw new RuntimeException("id not found "+id,ioe);
		}
		
		long lastModified = file.lastModified();
		sqlSourceVersion.put(id, lastModified);
//		InputStream ins = this.getClass().getResourceAsStream(
//				sqlRoot + File.separator + modelName + "md");
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
							sql.append(list.pollFirst() + lineSeparator);
						}
						sqlSourceMap.put(modelName + key, new SQLSource(modelName + key,
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
			sqlSourceMap.put(modelName + key,
					new SQLSource(modelName + key,sql.toString()));
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

	public Map<String, SQLSource> getSqlSourceMap() {
		return sqlSourceMap;
	}

//	@Override
//	public SQLSource getSelectByid(Class<?> cls) {
//		String className = cls.getSimpleName().toLowerCase();
//		SQLSource tempSource = sqlSourceMap.get(className + ".selectByid");
//		if (tempSource != null) {
//			return tempSource;
//		}
//		tempSource = dbs.generationSelectByid(cls);
//		sqlSourceMap.put(className + ".selectByid", tempSource);
//		return tempSource;
//	}
//
//	@Override
//	public SQLSource getSelectByTemplate(Class<?> cls) {
//		String className = cls.getSimpleName().toLowerCase();
//		SQLSource tempSource = sqlSourceMap.get(className
//				+ ".getByTemplate");
//		if (tempSource != null) {
//			return tempSource;
//		}
//		tempSource = dbs.generationSelectByTemplate(cls);
//		sqlSourceMap.put(className + ".getByTemplate", tempSource);
//		return tempSource;
//	}
//
//	@Override
//	public SQLSource getDeleteByid(Class<?> cls) {
//		String className = cls.getSimpleName().toLowerCase();
//		SQLSource tempSource = sqlSourceMap.get(className + ".deleteByid");
//		if (tempSource != null) {
//			return tempSource;
//		}
//		tempSource = dbs.generationDeleteByid(cls);
//		sqlSourceMap.put(className + ".deleteByid", tempSource);
//		return tempSource;
//	}
//
//	@Override
//	public SQLSource getSelectAll(Class<?> cls) {
//		String className = cls.getSimpleName().toLowerCase();
//		SQLSource tempSource = sqlSourceMap.get(className + ".selectAll");
//		if (tempSource != null) {
//			return tempSource;
//		}
//		tempSource = dbs.generationSelectAll(cls);
//		sqlSourceMap.put(className + ".selectAll", tempSource);
//		return tempSource;
//	}
//
//	@Override
//	public SQLSource getUpdateAll(Class<?> cls) {
//		String className = cls.getSimpleName().toLowerCase();
//		SQLSource tempSource = sqlSourceMap.get(className + ".updateAll");
//		if (tempSource != null) {
//			return tempSource;
//		}
//		tempSource = dbs.generationUpdateAll(cls);
//		sqlSourceMap.put(className + ".updateAll", tempSource);
//		return tempSource;
//	}
//
//	@Override
//	public SQLSource getUpdateByid(Class<?> cls) {
//		String className = cls.getSimpleName().toLowerCase();
//		SQLSource tempSource = sqlSourceMap.get(className + ".updateByid");
//		if (tempSource != null) {
//			return tempSource;
//		}
//		tempSource = dbs.generationUpdateByid(cls);
//		sqlSourceMap.put(className + ".updateByid", tempSource);
//		return tempSource;
//	}
//	@Override
//	public SQLSource getUpdateTemplate(Class<?> cls) {
//		String className = cls.getSimpleName().toLowerCase();
//		SQLSource tempSource = sqlSourceMap.get(className + ".updateTemplate");
//		if (tempSource != null) {
//			return tempSource;
//		}
//		tempSource = dbs.generationUpdateTemplate(cls);
//		sqlSourceMap.put(className + ".updateTemplate", tempSource);
//		return tempSource;
//	}
//	@Override
//	public SQLSource getBatchUpdateByid(Class<?> cls) {
//		String className = cls.getSimpleName().toLowerCase();
//		SQLSource tempSource = sqlSourceMap.get(className + ".batchUpdateByid");
//		if (tempSource != null) {
//			return tempSource;
//		}
//		tempSource = dbs.generationBatchUpdateByid(cls);
//		sqlSourceMap.put(className + ".batchUpdateByid", tempSource);
//		return tempSource;
//	}
//	@Override
//	public SQLSource getInsert(Class<?> cls) {
//		String className = cls.getSimpleName().toLowerCase();
//		SQLSource tempSource = sqlSourceMap.get(className + ".insert");
//		if (tempSource != null) {
//			return tempSource;
//		}
//		tempSource = dbs.generationInsert(cls);
//		sqlSourceMap.put(className + ".insert", tempSource);
//		return tempSource;
//	}


	
	private File getFile(String id){
		String modelName = id.substring(0, id.lastIndexOf(".") + 1);
		URL  url = this.getClass().getResource(
				sqlRoot + "/"+ modelName + "md");
		if(url==null) return null;
		File file = new File( url.getFile());
		return file;
	}
	@Override
	public boolean isAutoCheck() {
		return this.autoCheck;
	}
	@Override
	public void setAutoCheck(boolean check) {
		this.autoCheck = check;
		
	}
	
	
}
