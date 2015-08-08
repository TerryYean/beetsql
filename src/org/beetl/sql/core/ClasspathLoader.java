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
import java.util.List;
import java.util.Map;

import org.beetl.core.Configuration;
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

	private static ClasspathLoader classpathLoader = new ClasspathLoader();

	String sqlRoot = null;

	private static Map<String, SQLSource> sqlSourceMap = new HashMap<String, SQLSource>();

	private String STATEMENTSTART;// 定界符开始符号
	private String STATEMENTEND;// 定界符结束符号

	private String lineSeparator = System.getProperty("line.separator", "\n");

	private NameConversion nameConversion;

	private ClasspathLoader() {
		Configuration cf = Beetl.instance().getGroupTemplate().getConf();
		STATEMENTSTART = cf.getStatementStart();
		STATEMENTEND = cf.getStatementEnd();
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
		SQLSource ss = this.sqlSourceMap.get(id);
		if (ss == null) {
			loadSql(id);
		}
		ss = this.sqlSourceMap.get(id);
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

	/***
	 * 生成selectbyid语句
	 */
	@Override
	public SQLSource generationSelectByid(Class cls) {
		String className = cls.getSimpleName().toLowerCase();
		SQLSource tempSource = this.sqlSourceMap.get(className + ".selectByid");
		if (tempSource != null) {
			return tempSource;
		}
		String condition = null;
		List<String> ids = nameConversion.getId();
		if (ids.size() > 0) {
			String attrName = null;
			condition = " where 1=1";
			for (int i = 0; i < ids.size(); i++) {
				attrName = nameConversion.getPropertyName(ids.get(0));
				if (attrName != null) {
					condition = condition + " and " + ids.get(i) + "= ${"
							+ nameConversion.getPropertyName(ids.get(i))
							+ "}";
				}
			}
		}
		// 这一步还需不需要？
		if (condition == null) {
			condition = " where id=${id}";
		}
		String sql = "select * from " + nameConversion.getTableName(cls.getSimpleName()) + condition;
		tempSource = new SQLSource(sql);
		this.sqlSourceMap.put(className + ".selectByid", tempSource);
		return tempSource;
	}

	@Override
	public SQLSource generationSelectByTemplate(Class cls) {
		String className = cls.getSimpleName().toLowerCase();
		SQLSource tempSource = this.sqlSourceMap.get(className
				+ ".getByTemplate");
		if (tempSource != null) {
			return tempSource;
		}
		String fieldName = null;
		String condition = " where 1=1 " + lineSeparator;
		Method[] methods = cls.getDeclaredMethods();
		for (Method method : methods) {
			if(method.getName().startsWith("get")){
				fieldName = method.getName().substring(3);
				condition = condition + appendColumn(fieldName, "and");
			}
		}
		String sql = "select * from " + nameConversion.getTableName(cls.getSimpleName()) + condition;
		tempSource = new SQLSource(sql);
		this.sqlSourceMap.put(className + ".getByTemplate", tempSource);
		return tempSource;
	}

	@Override
	public SQLSource generationDeleteByid(Class cls) {
		String className = cls.getSimpleName().toLowerCase();
		SQLSource tempSource = this.sqlSourceMap.get(className + ".deleteByid");
		if (tempSource != null) {
			return tempSource;
		}
		String condition = null;
		List<String> ids = nameConversion.getId();
		if (ids.size() > 0) {
			String attrName = null;
			condition = " where 1=1";
			for (int i = 0; i < ids.size(); i++) {
				attrName = nameConversion.getPropertyName(ids.get(0));
				if (attrName != null) {
					condition = condition + " and " + ids.get(i) + "= ${"
							+ nameConversion.getPropertyName(ids.get(i))
							+ "}";
				}
			}
		}
		if (condition == null) {
			condition = " where id=${id}";
		}
		String sql = "delete from " + nameConversion.getTableName(cls.getSimpleName()) + condition;
		tempSource = new SQLSource(sql);
		this.sqlSourceMap.put(className + ".deleteByid", tempSource);
		return tempSource;
	}

	@Override
	public SQLSource generationSelectAll(Class cls) {
		String className = cls.getSimpleName().toLowerCase();
		SQLSource tempSource = this.sqlSourceMap.get(className + ".selectAll");
		if (tempSource != null) {
			return tempSource;
		}
		String sql = "select * from " + nameConversion.getTableName(cls.getSimpleName());
		tempSource = new SQLSource(sql);
		this.sqlSourceMap.put(className + ".selectAll", tempSource);
		return tempSource;
	}

	/****
	 * 自动生成update语句
	 */
	@Override
	public SQLSource generationUpdataByid(Class cls) {
		String className = cls.getSimpleName().toLowerCase();
		SQLSource tempSource = this.sqlSourceMap.get(className + ".updateByid");
		if (tempSource != null) {
			return tempSource;
		}
		String sql = "update " + nameConversion.getTableName(cls.getSimpleName()) + " set " + lineSeparator;
		String fieldName = null;
		String condition = null;
		
		Method[] methods = cls.getDeclaredMethods();
		for (Method method : methods) {
			if(method.getName().startsWith("get")){
				fieldName = method.getName().substring(3);
				sql = sql + appendColumn(fieldName, "and");
			}
		}
		List<String> ids = nameConversion.getId();
		if (ids.size() > 0) {
			String attrName = null;
			condition = " where 1=1";
			for (int i = 0; i < ids.size(); i++) {
				attrName = nameConversion.getPropertyName(ids.get(0));
				if (attrName != null) {
					condition = condition + " and " + ids.get(i) + "= ${"
							+ nameConversion.getPropertyName(ids.get(i))
							+ "}";
				}
			}
		}
		if (condition == null) {
			condition = " where id=${id}";
		}
		sql = removeComma(sql, condition);
		tempSource = new SQLSource(sql);
		this.sqlSourceMap.put(className + ".updateByid", tempSource);
		return tempSource;
	}

	@Override
	public SQLSource generationUpdataAll(Class cls) {
		String className = cls.getSimpleName().toLowerCase();
		SQLSource tempSource = this.sqlSourceMap.get(className + ".updateAll");
		if (tempSource != null) {
			return tempSource;
		}
		String sql = "update " + nameConversion.getTableName(cls.getSimpleName()) + " set " + lineSeparator;
		String fieldName = null;
		Method[] methods = cls.getDeclaredMethods();
		for (Method method : methods) {
			if(method.getName().startsWith("get")){
				fieldName = method.getName().substring(3);
				sql = sql + appendColumn(fieldName, ",");
			}
		}
		sql = removeComma(sql, null);
		tempSource = new SQLSource(sql);
		this.sqlSourceMap.put(className + ".updateAll", tempSource);
		return tempSource;
	}

	@Override
	public SQLSource generationUpdataByTemplate(Class cls) {
		// 等待思考
		String className = cls.getSimpleName().toLowerCase();
		SQLSource tempSource = this.sqlSourceMap.get(className
				+ ".updateBytemplate");
		// if (tempSource != null) {
		// return tempSource;
		// }
		// String sql = "update " + className + " set "+lineSeparator;
		// String fieldName = null;
		// String condition = " where 1=1 ";
		// Field[] fields = cls.getFields();
		// for (Field field : fields) {
		// fieldName = field.getName();
		// sql = appendColumn(sql, fieldName);
		// condition = condition +STATEMENTSTART + "if(!isEmpty(" + fieldName +
		// ")){"
		// + STATEMENTEND + " and "+fieldName + "=${" + fieldName +
		// "}"+lineSeparator
		// + STATEMENTSTART + "}" + STATEMENTEND;
		// }
		// sql = removeComma(sql,condition);
		// tempSource = new SQLSource(sql);
		// this.sqlSourceMap.put(className + ".updateBytemplate", tempSource);
		return tempSource;
	}

	/****
	 * 去掉逗号加上条件并换行
	 * 
	 * @param sql
	 * @return
	 */
	private String removeComma(String sql, String condition) {
		return sql.subSequence(0, sql.lastIndexOf(",")) + lineSeparator
				+ STATEMENTSTART + "} " + STATEMENTEND + lineSeparator
				+ (condition == null ? "" : condition);
	}

	/***
	 * 生成一个追加在sql后面的判断字段语句
	 * 
	 * @param sql
	 * @param fieldName
	 * @param connector
	 *            连接字段间的符号，如果是逗号则放在字段后面，否则放在字段前面（如 and，or）
	 * @return
	 */
	private String appendColumn(String fieldName, String connector) {
		String colName = nameConversion.getColName(fieldName);
		if (colName != null) {
			if (connector.equals(",")) {
				return STATEMENTSTART + "if(!isEmpty(" + fieldName + ")){"
						+ STATEMENTEND + colName + "=${" + fieldName + "},"
						+ lineSeparator + STATEMENTSTART + "}" + STATEMENTEND;
			}
			connector = " " + connector + " ";
			return STATEMENTSTART + "if(!isEmpty(" + fieldName + ")){"
					+ STATEMENTEND + connector + colName + "=${" + fieldName
					+ "}" + lineSeparator + STATEMENTSTART + "}" + STATEMENTEND;
		}
		return "";
	}
	/****
	 * insert等待实现
	 * @return
	 */
	
	
	
	public Map<String, SQLSource> getSqlSourceMap() {
		return sqlSourceMap;
	}

	public String getSTATEMENTSTART() {
		return STATEMENTSTART;
	}

	public String getSTATEMENTEND() {
		return STATEMENTEND;
	}

	public NameConversion getNameConversion() {
		return nameConversion;
	}

	public void setNameConversion(NameConversion nameConversion) {
		this.nameConversion = nameConversion;
	}

}
