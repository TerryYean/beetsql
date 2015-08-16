package org.beetl.sql.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

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

	private static ClasspathLoader classpathLoader = new ClasspathLoader();

	String sqlRoot = null;

	private String lineSeparator = System.getProperty("line.separator", "\n");

	private static Map<String, SQLSource> sqlSourceMap = new HashMap<String, SQLSource>();

	private DBStyle dbs = new MySqlStyle();//默认mysql？

	private ClasspathLoader() {
		
	}
	public static ClasspathLoader instance(String sqlRoot) {
		if (classpathLoader == null) {
			classpathLoader = new ClasspathLoader();
		}
		classpathLoader.sqlRoot = sqlRoot;
		return classpathLoader;
	}

	@Override
	public SQLSource getSQL(String id) {
		// real path = sqlRoot\xx\yy.sql
		SQLSource ss = sqlSourceMap.get(id);
		if (ss == null) {
			loadSql(id);
		}
		ss = sqlSourceMap.get(id);
		return ss;
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
							sql.append(list.pollFirst() + lineSeparator);
						}
						sqlSourceMap.put(modelName + key, new SQLSource(
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

	public Map<String, SQLSource> getSqlSourceMap() {
		return sqlSourceMap;
	}

	@Override
	public SQLSource getSelectByid(Class<?> cls) {
		String className = cls.getSimpleName().toLowerCase();
		SQLSource tempSource = sqlSourceMap.get(className + ".selectByid");
		if (tempSource != null) {
			return tempSource;
		}
		tempSource = dbs.generationSelectByid(cls);
		sqlSourceMap.put(className + ".selectByid", tempSource);
		return tempSource;
	}

	@Override
	public SQLSource getSelectByTemplate(Class<?> cls) {
		String className = cls.getSimpleName().toLowerCase();
		SQLSource tempSource = sqlSourceMap.get(className
				+ ".getByTemplate");
		if (tempSource != null) {
			return tempSource;
		}
		tempSource = dbs.generationSelectByTemplate(cls);
		sqlSourceMap.put(className + ".getByTemplate", tempSource);
		return tempSource;
	}

	@Override
	public SQLSource getDeleteByid(Class<?> cls) {
		String className = cls.getSimpleName().toLowerCase();
		SQLSource tempSource = sqlSourceMap.get(className + ".deleteByid");
		if (tempSource != null) {
			return tempSource;
		}
		tempSource = dbs.generationDeleteByid(cls);
		sqlSourceMap.put(className + ".deleteByid", tempSource);
		return tempSource;
	}

	@Override
	public SQLSource getSelectAll(Class<?> cls) {
		String className = cls.getSimpleName().toLowerCase();
		SQLSource tempSource = sqlSourceMap.get(className + ".selectAll");
		if (tempSource != null) {
			return tempSource;
		}
		tempSource = dbs.generationSelectAll(cls);
		sqlSourceMap.put(className + ".selectAll", tempSource);
		return tempSource;
	}

	@Override
	public SQLSource getUpdateAll(Class<?> cls) {
		String className = cls.getSimpleName().toLowerCase();
		SQLSource tempSource = sqlSourceMap.get(className + ".updateAll");
		if (tempSource != null) {
			return tempSource;
		}
		tempSource = dbs.generationUpdateAll(cls);
		sqlSourceMap.put(className + ".updateAll", tempSource);
		return tempSource;
	}

	@Override
	public SQLSource getUpdateByid(Class<?> cls) {
		String className = cls.getSimpleName().toLowerCase();
		SQLSource tempSource = sqlSourceMap.get(className + ".updateByid");
		if (tempSource != null) {
			return tempSource;
		}
		tempSource = dbs.generationUpdateByid(cls);
		sqlSourceMap.put(className + ".updateByid", tempSource);
		return tempSource;
	}
	@Override
	public SQLSource getUpdateTemplate(Class<?> cls) {
		String className = cls.getSimpleName().toLowerCase();
		SQLSource tempSource = sqlSourceMap.get(className + ".updateTemplate");
		if (tempSource != null) {
			return tempSource;
		}
		tempSource = dbs.generationUpdateTemplate(cls);
		sqlSourceMap.put(className + ".updateTemplate", tempSource);
		return tempSource;
	}
	@Override
	public SQLSource getBatchUpdateByid(Class<?> cls) {
		String className = cls.getSimpleName().toLowerCase();
		SQLSource tempSource = sqlSourceMap.get(className + ".batchUpdateByid");
		if (tempSource != null) {
			return tempSource;
		}
		tempSource = dbs.generationBatchUpdateByid(cls);
		sqlSourceMap.put(className + ".batchUpdateByid", tempSource);
		return tempSource;
	}
	@Override
	public SQLSource getInsert(Class<?> cls) {
		String className = cls.getSimpleName().toLowerCase();
		SQLSource tempSource = sqlSourceMap.get(className + ".insert");
		if (tempSource != null) {
			return tempSource;
		}
		tempSource = dbs.generationInsert(cls);
		sqlSourceMap.put(className + ".insert", tempSource);
		return tempSource;
	}

	public DBStyle getDbs() {
		return dbs;
	}

	public void setDbs(DBStyle dbs) {
		this.dbs = dbs;
	}
}
