package org.beetl.sql.core.db;

import java.lang.reflect.Method;
import java.util.List;

import org.beetl.core.Configuration;
import org.beetl.sql.core.NameConversion;
import org.beetl.sql.core.SQLSource;
import org.beetl.sql.core.annotatoin.Table;
import org.beetl.sql.core.engine.Beetl;
import org.beetl.sql.core.kit.StringKit;
/**
 * 按照mysql来的，oralce需要重载insert，page方法
 * @author xiandafu
 *
 */
public abstract class AbstractDBStyle implements DBStyle {
	
	protected static AbstractDBStyle adbs;
	protected NameConversion nameConversion;
	protected MetadataManager metadataManager;
	protected String STATEMENT_START;// 定界符开始符号
	protected String STATEMENT_END;// 定界符结束符号
	protected String HOLDER_START;// 站位符开始符号
	protected String HOLDER_END;// 站位符结束符号
	protected String lineSeparator = System.getProperty("line.separator", "\n");
	
	public AbstractDBStyle() {
	
	}
	
	@Override
	public void init(Beetl beetl){
		Configuration cf =beetl.getGroupTemplate().getConf();
		STATEMENT_START = cf.getStatementStart();
		STATEMENT_END = cf.getStatementEnd();
		if(STATEMENT_END==null||STATEMENT_END.length()==0){
			STATEMENT_END = lineSeparator;
		}
		HOLDER_START = cf.getPlaceholderStart();
		HOLDER_END = cf.getPlaceholderEnd();
	}

	public String getSTATEMENTSTART() {
		return STATEMENT_START;
	}

	public String getSTATEMENTEND() {
		return STATEMENT_END;
	}
	
	@Override
	public NameConversion getNameConversion() {
		return nameConversion;
	}

	@Override
	public void setNameConversion(NameConversion nameConversion) {
		this.nameConversion = nameConversion;
	}

	@Override
	public MetadataManager getMetadataManager() {
		return metadataManager;
	}

	@Override
	public void setMetadataManager(MetadataManager metadataManager) {
		this.metadataManager = metadataManager;
	}

	@Override
	public SQLSource genSelectById(Class<?> cls) {
		String tableName = nameConversion.getTableName(cls);
		String condition = appendIdCondition(cls);
		return new SQLSource(new StringBuffer("select * from ").append(tableName).append(condition).toString());
	}

	@Override
	public SQLSource genSelectByTemplate(Class<?> cls) {
		String fieldName = null;
		String condition = " where 1=1 " + lineSeparator;
		Method[] methods = cls.getDeclaredMethods();
		String tableName = nameConversion.getTableName(cls);
		for (Method method : methods) {
			if(method.getName().startsWith("get")){
				fieldName = StringKit.toLowerCaseFirstOne(method.getName().substring(3));
				condition = condition + appendWhere(cls,tableName, fieldName);
			}
		}
		return new SQLSource(new StringBuffer("select * from ").append(tableName).append(condition).toString());
	}
	
	@Override
	public SQLSource genSelectCountByTemplate(Class<?> cls){
		String fieldName = null;
		String condition = " where 1=1 " + lineSeparator;
		Method[] methods = cls.getDeclaredMethods();
		String tableName = nameConversion.getTableName(cls);
		for (Method method : methods) {
			if(method.getName().startsWith("get")){
				fieldName = StringKit.toLowerCaseFirstOne(method.getName().substring(3));
				condition = condition + appendWhere(cls,tableName, fieldName);
			}
		}
		return new SQLSource(new StringBuffer("select count(*) from ").append(tableName).append(condition).toString());

	}

	@Override
	public SQLSource genDeleteById(Class<?> cls) {
		String tableName = nameConversion.getTableName(cls);
		String condition = appendIdCondition(cls);
		return new SQLSource(new StringBuffer("delete from ").append(tableName).append(condition).toString());
	}

	@Override
	public SQLSource genSelectAll(Class<?> cls) {
		return new SQLSource(new StringBuffer("select * from ").append(nameConversion.getTableName(cls)).toString());
	}

	@Override
	public SQLSource genUpdateById(Class<?> cls) {
		String tableName = nameConversion.getTableName(cls);
		StringBuilder sql = new StringBuilder("update ").append(tableName).append(" set ").append(lineSeparator);
		String fieldName = null;
		
		Method[] methods = cls.getDeclaredMethods();
		for (Method method : methods) {
			if(method.getName().startsWith("get") && !method.getName().endsWith("Id")){//TODO 暂时限定排除ID
				fieldName = StringKit.toLowerCaseFirstOne(method.getName().substring(3));
				sql.append(appendSetColumnAbsolute(cls,tableName, fieldName));
			}
		}
		String condition = appendIdCondition(cls);
		sql = removeComma(sql, condition);
		return new SQLSource(sql.toString());
	}
	
	@Override
	public SQLSource genUpdateTemplate (Class<?> cls) {
		String tableName = nameConversion.getTableName(cls);
		StringBuilder sql = new StringBuilder("update ").append(tableName).append(" set ").append(lineSeparator);
		String fieldName = null;
		String condition = " where 1=1 " + lineSeparator;
		Method[] methods = cls.getDeclaredMethods();
		for (Method method : methods) {
			if(method.getName().startsWith("get")){
				fieldName = StringKit.toLowerCaseFirstOne(method.getName().substring(3));
				sql.append(appendSetColumn(cls,tableName, fieldName,"para"));
				condition = condition + appendWhere(cls,tableName, fieldName,"condition");
			}
		}
		sql = removeComma(sql, condition);
		return new SQLSource(sql.toString());
	}

	@Override
	public SQLSource genUpdateAll(Class<?> cls) {
		String tableName = nameConversion.getTableName(cls);
		StringBuilder sql = new StringBuilder("update ").append(tableName).append(" set ").append(lineSeparator);
		String fieldName = null;
		Method[] methods = cls.getDeclaredMethods();
		for (Method method : methods) {
			if(method.getName().startsWith("get")){
				fieldName = StringKit.toLowerCaseFirstOne(method.getName().substring(3));
				sql.append(appendSetColumn(cls,tableName, fieldName));
			}
		}
		sql = removeComma(sql, null);
		return new SQLSource(sql.toString());
	}

	@Override
	public SQLSource genInsert(Class<?> cls) {
		String tableName = nameConversion.getTableName(cls);
		StringBuilder sql = new StringBuilder("insert into " + tableName + lineSeparator);
		StringBuilder colSql = new StringBuilder("(");
		StringBuilder valSql = new StringBuilder(" VALUES (");
		String fieldName = null;
		int idType = DBStyle.ID_ASSIGN ;
		Method[] methods = cls.getDeclaredMethods();
		for (Method method : methods) {
			if(method.getName().startsWith("get")){
				fieldName = StringKit.toLowerCaseFirstOne(method.getName().substring(3));
				List<String> ids = this.metadataManager.getIds(tableName);
				if(ids.contains(fieldName)){
					idType = this.getIdType(method);
					if(idType==DBStyle.ID_AUTO){
						continue ; //忽略这个字段
					}else if(idType==DBStyle.ID_SEQ){
						if(ids.size()!=1) throw new RuntimeException("序列期望一个，但有"+ids);
						colSql.append(appendInsertColumn(cls,tableName, fieldName));
						valSql.append( HOLDER_START+ "_tempKey" + HOLDER_END+",");
						continue;
					}else if(idType==DBStyle.ID_ASSIGN){
						//normal
					}
				}
				colSql.append(appendInsertColumn(cls,tableName, fieldName));
				valSql.append(appendInsertVlaue(cls,tableName, fieldName));
			}
		}
		sql.append(removeComma(colSql, null).append(")").append(removeComma(valSql, null)).append(")").toString());
		SQLSource source = new SQLSource(sql.toString());
		source.setIdType(idType);
		return source;
	}
	
	/****
	 * 去掉逗号后面的加上结束符和条件并换行
	 * 
	 * @param sql
	 * @return
	 */
	private StringBuilder removeComma(StringBuilder sql, String condition) {
		return sql.deleteCharAt(sql.lastIndexOf(",")).append((condition == null ? "" : condition));
	}

	/***
	 * 生成一个追加在set子句的后面sql(示例：name=${name},)
	 * @param tableName
	 * @param fieldName
	 * @return
	 */
	private String appendSetColumnAbsolute(Class c,String tableName,String fieldName) {
		String colName = nameConversion.getColName(c,fieldName);
		if (metadataManager.existColName(tableName, colName)) {
			return colName + "="+HOLDER_START + fieldName + HOLDER_END+",";
		}
		return "";
	}
	
	/***
	 * 生成一个追加在set子句的后面sql(示例：name=${name},)有Empty判断
	 * @param tableName
	 * @param fieldName
	 * @return
	 */
	private String appendSetColumn(Class c,String tableName,String fieldName,String...prefixs) {
		String prefix = "";
		if(prefixs.length > 0){
			prefix = prefixs[0]+".";
		}
		String colName = nameConversion.getColName(c,fieldName);
		if (metadataManager.existColName(tableName, colName)) {
			return STATEMENT_START + "if(!isEmpty(" + prefix+fieldName + ")){"
					+ STATEMENT_END + "\t" + colName + "="+HOLDER_START + prefix+fieldName + HOLDER_END+","
					+ lineSeparator + STATEMENT_START + "}" + STATEMENT_END;
		}
		return "";
	}
	
	/*****
	 * 生成一个追加在where子句的后面sql(示例：name=${name} and)
	 * @param tableName
	 * @param fieldName
	 * @return
	 */
	private String appendWhere(Class c,String tableName,String fieldName,String...prefixs) {
		String prefix = "";
		if(prefixs.length > 0){
			prefix = prefixs[0]+".";
		}
		String colName = nameConversion.getColName(c,fieldName);
		String connector = " and ";
		if (metadataManager.existColName(tableName, colName)) {
			return STATEMENT_START + "if(!isEmpty(" + prefix+fieldName + ")){"
					+ STATEMENT_END + connector + colName + "="+HOLDER_START + prefix+fieldName
					+ HOLDER_END+ lineSeparator + STATEMENT_START + "}" + STATEMENT_END;
		}
		return "";
	}
	
	/****
	 * 生成一个追加在insert into 子句的后面sql(示例：name,)
	 * @param tableName
	 * @param fieldName
	 * @return
	 */
	private String appendInsertColumn(Class c,String tableName,String fieldName) {
		String colName = nameConversion.getColName(c,fieldName);
		if (metadataManager.existColName(tableName, colName)) {
			return  colName + ",";
		}
		return "";
	}
	
	/****
	 * 生成一个追加在insert into value子句的后面sql(示例：name=${name},)
	 * @param tableName
	 * @param fieldName
	 * @return
	 */
	private String appendInsertVlaue(Class c,String tableName,String fieldName) {
		String colName = nameConversion.getColName(c,fieldName);
		if (metadataManager.existColName(tableName, colName)) {
			return  HOLDER_START+ fieldName + HOLDER_END+",";
		}
		return "";
	}
	
	/***
	 * 生成主键条件子句（示例 whrer 1=1 and id=${id}）
	 * @param tableName
	 * @return
	 */
	private String appendIdCondition(Class<?> cls) {
		String tableName = nameConversion.getTableName(cls);
		String condition = null;
		List<String> ids = metadataManager.getIds(tableName);
		if (ids.size() > 0) {
			String attrName = null;
			condition = " where 1=1";
			for (int i = 0; i < ids.size(); i++) {
				attrName = nameConversion.getPropertyName(cls,ids.get(i));
				if (metadataManager.existPropertyName(cls, attrName)) {
					condition = condition + " and " + ids.get(i) + "= "+HOLDER_START
							+ attrName
							+ HOLDER_END;
				}
			}
		}
		return condition;
	}
	
	/****
	 * 生成一个循环读取Id列表
	 * @param tableName
	 * @param fieldName
	 * @return
	 */
	private String appendIdList(String idName) {
		return new StringBuilder(lineSeparator).append(STATEMENT_START)
				.append("trim(){for(obj in map){").append(STATEMENT_END)
				.append(HOLDER_START+ "obj."+idName + HOLDER_END+",").append(lineSeparator)
				.append(STATEMENT_START).append("}}").append(STATEMENT_END).toString();
	}
	
	private String getTableName(Class c){
		
		return nameConversion.getTableName(c);
		
	}
	

}