package org.beetl.sql.core.db;

import java.lang.reflect.Method;
import java.util.List;

import org.beetl.core.Configuration;
import org.beetl.sql.core.Beetl;
import org.beetl.sql.core.NameConversion;
import org.beetl.sql.core.SQLSource;
import org.beetl.sql.core.kit.StringKit;
/**
 * 按照mysql来的，oralce需要重载insert，page方法
 * @author xiandafu
 *
 */
public abstract class AbstractDBStyle implements DBStyle {
	
	protected NameConversion nameConversion;
	protected MetadataManager metadataManager;
	protected String STATEMENTSTART;// 定界符开始符号
	protected String STATEMENTEND;// 定界符结束符号

	protected String lineSeparator = System.getProperty("line.separator", "\n");
	
	protected static AbstractDBStyle adbs;
	
	public AbstractDBStyle() {
		Configuration cf = Beetl.instance().getGroupTemplate().getConf();
		STATEMENTSTART = cf.getStatementStart();
		STATEMENTEND = cf.getStatementEnd();
	}
	public abstract AbstractDBStyle instance();

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

	@Override
	/**
	 * 获得一个序列号获取，考虑到跨平台，提供一个name，对于mysql，总是忽略，对于oralce，是其序列号
	 */
	public KeyHolder getKeyHolder(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public KeyHolder getKeyHolder(){
		
		return null;
	}
	

	@Override
	public SQLSource getInsert(Class<?> c) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLSource getSelectById(Class<?> c) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/***
	 * 生成selectbyid语句
	 */
	@Override
	public SQLSource generationSelectByid(Class<?> cls) {
		String tableName = nameConversion.getTableName(cls.getSimpleName());
		String condition = appendIdCondition(cls);
		// 这一步还需不需要？
		if (condition == null) {
			condition = " where id=${id}";
		}
		return new SQLSource(new StringBuffer("select * from ").append(tableName).append(condition).toString());
	}

	@Override
	public SQLSource generationSelectByTemplate(Class<?> cls) {
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
		return new SQLSource(new StringBuffer("select * from ").append(tableName).append(condition).toString());
	}

	@Override
	public SQLSource generationDeleteByid(Class<?> cls) {
		String tableName = nameConversion.getTableName(cls.getSimpleName());
		String condition = appendIdCondition(cls);
		if (condition == null) {
			condition = " where id=${id}";
		}
		return new SQLSource(new StringBuffer("delete from ").append(tableName).append(condition).toString());
	}

	@Override
	public SQLSource generationSelectAll(Class<?> cls) {
		return new SQLSource(new StringBuffer("select * from ").append(nameConversion.getTableName(cls.getSimpleName())).toString());
	}

	/****
	 * 自动生成update语句
	 */
	@Override
	public SQLSource generationUpdataByid(Class<?> cls) {
		String tableName = nameConversion.getTableName(cls.getSimpleName());
		StringBuilder sql = new StringBuilder("update ").append(tableName).append(" set ").append(lineSeparator);
		String fieldName = null;
		
		Method[] methods = cls.getDeclaredMethods();
		for (Method method : methods) {
			if(method.getName().startsWith("get")){
				fieldName = StringKit.toLowerCaseFirstOne(method.getName().substring(3));
				sql.append(appendSetColumn(tableName, fieldName));
			}
		}
		String condition = appendIdCondition(cls);
		if (condition == null) {
			condition = " where id=${id}";
		}
		sql = removeComma(sql, condition);
		return new SQLSource(sql.toString());
	}

	@Override
	public SQLSource generationUpdataAll(Class<?> cls) {
		String tableName = nameConversion.getTableName(cls.getSimpleName());
		StringBuilder sql = new StringBuilder("update ").append(tableName).append(" set ").append(lineSeparator);
		String fieldName = null;
		Method[] methods = cls.getDeclaredMethods();
		for (Method method : methods) {
			if(method.getName().startsWith("get")){
				fieldName = StringKit.toLowerCaseFirstOne(method.getName().substring(3));
				sql.append(appendSetColumn(tableName, fieldName));
			}
		}
		sql = removeComma(sql, null);
		return new SQLSource(sql.toString());
	}
	/****
	 * 生成insert语句
	 * @return
	 */
	public SQLSource generationInsert(Class<?> cls) {
		String tableName = nameConversion.getTableName(cls.getSimpleName());
		StringBuilder sql = new StringBuilder("insert into " + tableName + lineSeparator);
		StringBuilder colSql = new StringBuilder("(");
		StringBuilder valSql = new StringBuilder(" VALUES (");
		String fieldName = null;
		Method[] methods = cls.getDeclaredMethods();
		for (Method method : methods) {
			if(method.getName().startsWith("get")){
				fieldName = StringKit.toLowerCaseFirstOne(method.getName().substring(3));
				colSql.append(appendInsertColumn(tableName, fieldName));
				valSql.append(appendInsertVlaue(tableName, fieldName));
			}
		}
		sql.append(removeComma(colSql, null).append(")").append(removeComma(valSql, null)).append(")").toString());
		return new SQLSource(sql.toString());
	}
	/****
	 * 去掉逗号加上条件并换行
	 * 
	 * @param sql
	 * @return
	 */
	private StringBuilder removeComma(StringBuilder sql, String condition) {
		return sql.delete(sql.lastIndexOf(","),sql.length() ).append(lineSeparator
				+ STATEMENTSTART + "} " + STATEMENTEND + lineSeparator
				+ (condition == null ? "" : condition));
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
	 * 生成主键条件子句（示例 whrer 1=1 and id=${id}）
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

}