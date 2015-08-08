package org.beetl.sql.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.beetl.core.Configuration;
import org.beetl.sql.core.kit.StringKit;

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
	private MetadataManager metadataManager;
	
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
		String tableName = nameConversion.getTableName(cls.getSimpleName());
		String condition = appendIdCondition(cls);
		// 这一步还需不需要？
		if (condition == null) {
			condition = " where id=${id}";
		}
		String sql = "select * from " + tableName + condition;
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
		String tableName = nameConversion.getTableName(cls.getSimpleName());
		for (Method method : methods) {
			if(method.getName().startsWith("get")){
				fieldName = StringKit.toLowerCaseFirstOne(method.getName().substring(3));
				condition = condition + appendWhere(tableName, fieldName);
			}
		}
		String sql = "select * from " + tableName + condition;
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
		String tableName = nameConversion.getTableName(cls.getSimpleName());
		String condition = appendIdCondition(cls);
		if (condition == null) {
			condition = " where id=${id}";
		}
		String sql = "delete from " + tableName + condition;
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
		String tableName = nameConversion.getTableName(cls.getSimpleName());
		String sql = "update " + tableName + " set " + lineSeparator;
		String fieldName = null;
		
		Method[] methods = cls.getDeclaredMethods();
		for (Method method : methods) {
			if(method.getName().startsWith("get")){
				fieldName = StringKit.toLowerCaseFirstOne(method.getName().substring(3));
				sql = sql + appendSetColumn(tableName, fieldName);
			}
		}
		String condition = appendIdCondition(cls);
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
		String tableName = nameConversion.getTableName(cls.getSimpleName());
		String sql = "update " + tableName + " set " + lineSeparator;
		String fieldName = null;
		Method[] methods = cls.getDeclaredMethods();
		for (Method method : methods) {
			if(method.getName().startsWith("get")){
				fieldName = StringKit.toLowerCaseFirstOne(method.getName().substring(3));
				sql = sql + appendSetColumn(tableName, fieldName);
			}
		}
		sql = removeComma(sql, null);
		tempSource = new SQLSource(sql);
		this.sqlSourceMap.put(className + ".updateAll", tempSource);
		return tempSource;
	}
	/****
	 * 生成insert语句
	 * @return
	 */
	public SQLSource generationInsert(Class cls) {
		String className = cls.getSimpleName().toLowerCase();
		SQLSource tempSource = this.sqlSourceMap.get(className + ".insert");
		if (tempSource != null) {
			return tempSource;
		}
		String tableName = nameConversion.getTableName(cls.getSimpleName());
		String sql = "insert into " + tableName + lineSeparator;
		String colSql = "(";
		String valSql = " VALUES (";
		String fieldName = null;
		Method[] methods = cls.getDeclaredMethods();
		for (Method method : methods) {
			if(method.getName().startsWith("get")){
				fieldName = StringKit.toLowerCaseFirstOne(method.getName().substring(3));
				colSql = colSql + appendInsertColumn(tableName, fieldName);
				valSql = valSql + appendInsertVlaue(tableName, fieldName);
			}
		}
		sql = sql + removeComma(colSql, null)+")"+removeComma(valSql, null)+")";
		tempSource = new SQLSource(sql);
		this.sqlSourceMap.put(className + ".insert", tempSource);
		return tempSource;
	}
	/****
	 * 去掉逗号加上条件并换行
	 * 
	 * @param sql
	 * @return
	 */
	private String removeComma(String sql, String condition) {
		return sql.substring(0, sql.lastIndexOf(",")) + lineSeparator
				+ STATEMENTSTART + "} " + STATEMENTEND + lineSeparator
				+ (condition == null ? "" : condition);
	}

	/***
	 * 生成一个追加在set子句的后面sql(示例：name=${name},)
	 * @param tableName
	 * @param fieldName
	 * @return
	 */
	private String appendSetColumn(String tableName,String fieldName) {
		String colName = nameConversion.getColName(fieldName);
		if (metadataManager.existColName(tableName, colName)) {
			return STATEMENTSTART + "if(!isEmpty(" + fieldName + ")){"
					+ STATEMENTEND + colName + "=${" + fieldName + "},"
					+ lineSeparator + STATEMENTSTART + "}" + STATEMENTEND;
		}
		return "";
	}
	/*****
	 * 生成一个追加在where子句的后面sql(示例：name=${name} and)
	 * @param tableName
	 * @param fieldName
	 * @return
	 */
	private String appendWhere(String tableName,String fieldName) {
		String colName = nameConversion.getColName(fieldName);
		String connector = " and ";
		if (metadataManager.existColName(tableName, colName)) {
			return STATEMENTSTART + "if(!isEmpty(" + fieldName + ")){"
					+ STATEMENTEND + connector + colName + "=${" + fieldName
					+ "}" + lineSeparator + STATEMENTSTART + "}" + STATEMENTEND;
		}
		return "";
	}
	/****
	 * 生成一个追加在insert into 子句的后面sql(示例：name,)
	 * @param tableName
	 * @param fieldName
	 * @return
	 */
	private String appendInsertColumn(String tableName,String fieldName) {
		String colName = nameConversion.getColName(fieldName);
		if (metadataManager.existColName(tableName, colName)) {
			return STATEMENTSTART + "if(!isEmpty(" + fieldName + ")){"
					+ STATEMENTEND + colName + ","
					+ lineSeparator + STATEMENTSTART + "}" + STATEMENTEND;
		}
		return "";
	}
	/****
	 * 生成一个追加在insert into value子句的后面sql(示例：name=${name},)
	 * @param tableName
	 * @param fieldName
	 * @return
	 */
	private String appendInsertVlaue(String tableName,String fieldName) {
		String colName = nameConversion.getColName(fieldName);
		if (metadataManager.existColName(tableName, colName)) {
			return STATEMENTSTART + "if(!isEmpty(" + fieldName + ")){"
					+ STATEMENTEND + "${" + fieldName + "},"
					+ lineSeparator + STATEMENTSTART + "}" + STATEMENTEND;
		}
		return "";
	}
	/***
	 * 生成根据where主键语句
	 * @param tableName
	 * @return
	 */
	private String appendIdCondition(Class<?> cls) {
		String tableName = nameConversion.getTableName(cls.getSimpleName());
		String condition = null;
		List<String> ids = metadataManager.getIds(tableName);
		if (ids.size() > 0) {
			String attrName = null;
			condition = " where 1=1";
			for (int i = 0; i < ids.size(); i++) {
				attrName = nameConversion.getPropertyName(ids.get(i));
				if (metadataManager.existPropertyName(cls, attrName)) {
					condition = condition + " and " + ids.get(i) + "= ${"
							+ attrName
							+ "}";
				}
			}
		}
		return condition;
	}
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

	public MetadataManager getMetadataManager() {
		return metadataManager;
	}

	public void setMetadataManager(MetadataManager metadataManager) {
		this.metadataManager = metadataManager;
	}
	
}
