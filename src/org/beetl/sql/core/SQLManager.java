package org.beetl.sql.core;

import java.util.List;
import java.util.Map;

import org.beetl.sql.core.db.DBStyle;
import org.beetl.sql.core.db.MetadataManager;
import org.beetl.sql.core.kit.MapKit;

public class SQLManager {
	
	private DBStyle dbStyle;
	private SQLLoader sqlLoader;
	private ConnectionSource ds = null;//数据库连接管理 TODO 应该指定一个默认数据库连接管理
	NameConversion nc = null;//名字转换器
	Interceptor[] inters = {};
	public static final int SELECT_BY_ID = 0;
	public static final int SELECT_BY_TEMPLATE = 1;
	public static final int DELETE_BY_ID = 2;
	public static final int SELECT_ALL = 3;
	public static final int UPDATE_ALL = 4;
	public static final int UPDATE_BY_ID = 5;
	public static final int INSERT = 6;

	public SQLManager(DBStyle dbStyle, SQLLoader sqlLoader, ConnectionSource ds) {
		this.dbStyle = dbStyle;
		this.sqlLoader = sqlLoader;
		this.ds = ds;
		this.nc = new HumpNameConversion();
		this.dbStyle.setNameConversion(this.nc);
		this.dbStyle.setMetadataManager(new MetadataManager(this.ds));
		this.sqlLoader.setDbs(dbStyle);
	}
	
	public SQLManager(DBStyle dbStyle, SQLLoader sqlLoader,
			ConnectionSource ds, NameConversion nc, Interceptor[] inters) {
		this.dbStyle = dbStyle;
		this.sqlLoader = sqlLoader;
		this.ds = ds;
		this.nc = nc;
		this.inters = inters;
		this.dbStyle.setNameConversion(this.nc);
		this.dbStyle.setMetadataManager(new MetadataManager(this.ds));
		this.sqlLoader.setDbs(dbStyle);
	}

	public SQLResult getSQLResult(String id, Map<String, Object> paras) {
		SQLScript script = getScript(id);
		return script.run(paras);
	}

	public SQLScript getScript(String id) {
		String template = sqlLoader.getSQL(id).getTemplate();
		SQLScript script = new SQLScript(template, this);
		script.setId(id);
		return script;
	}

	/**
	 * 生成增删改查模板
	 * @param cls
	 * @param tempId
	 * @return
	 */
	public SQLScript getScript(Class<?> cls, int tempId) {
		switch (tempId) {
			case SELECT_BY_ID: {
				String template = sqlLoader.getSelectByid(cls).getTemplate();
				SQLScript script = new SQLScript(template, this);
				return script;
			}
			case SELECT_BY_TEMPLATE: {
				String template = sqlLoader.getSelectByTemplate(cls)
						.getTemplate();
				SQLScript script = new SQLScript(template, this);
				return script;
			}
			case DELETE_BY_ID: {
				String template = sqlLoader.getDeleteByid(cls).getTemplate();
				SQLScript script = new SQLScript(template, this);
				return script;
			}
			case SELECT_ALL: {
				String template = sqlLoader.getSelectAll(cls).getTemplate();
				SQLScript script = new SQLScript(template, this);
				return script;
			}
			case UPDATE_ALL: {
				String template = sqlLoader.getUpdataAll(cls).getTemplate();
				SQLScript script = new SQLScript(template, this);
				return script;
			}
			case UPDATE_BY_ID: {
				String template = sqlLoader.getUpdataByid(cls).getTemplate();
				SQLScript script = new SQLScript(template, this);
				return script;
			}
			case INSERT: {
				SQLSource source = sqlLoader.getInsert(cls);
				SQLScript script = new SQLScript(source, this);
				return script;
			}
			default: {
				throw new UnsupportedOperationException();
			}
		}
	}
	
	/****
	 * 获取为分页语句
	 * @param sql
	 * @return
	 */
	public SQLScript getPageSqlScript(String sql) {
		if(!sql.toLowerCase().startsWith("select")){
			throw new UnsupportedOperationException();
		}
		return new SQLScript(dbStyle.getPageSQL(sql), this);
	}
	
	/****
	 * 获取总行数语句
	 * @param sql
	 * @return
	 */
	public SQLScript getCountSqlScript(String sql) {
		if(!sql.toLowerCase().startsWith("select")){
			throw new UnsupportedOperationException();
		}
		sql = "select count(*) "+sql.toLowerCase().substring(sql.indexOf("from"));
		return new SQLScript(sql, this);
	}
	
	/**
	 * 通过sqlId进行查询:查询自定义语句
	 * @param sqlId
	 * @param clazz
	 * @param paras
	 * @return List<Pojo>
	 */
	public <T> List<T> selectBySqlId(String sqlId, Class<T> clazz, Map<String, Object> paras) { 
		
		SQLScript script = getScript(sqlId);
		
		return script.select(clazz, paras);
	}
	
	/**
	 * 
	 * select * from use
	 * 
	 * btsql自动生成查询语句
	 * @param tempId
	 * @param clazz
	 * @param paras
	 * @return
	 */
	public <T> List<T> selectAll(Class<T> clazz) {

		SQLScript script = getScript(clazz, SQLManager.SELECT_ALL);

		return script.select(clazz, null);
	}
	
	/**
	 * 
	 * select * from user where 1=1 and id= #id#
	 * 
	 * 获取唯一记录
	 * @param clazz
	 * @param value
	 * @return
	 */
	public <T> T selectById(Class<T> clazz, Object ...pkValues) {
		
		SQLScript script = getScript(clazz, SQLManager.SELECT_BY_ID);
		
		return script.unique(clazz, pkValues);
	}
	
	/**
	 * 
	 * select * from user where 1=1 
		@if(!isEmpty(name)){
		 and name=#name#
		@}
		@if(!isEmpty(id)){
		 and id=#id#
		@}
		@if(!isEmpty(age)){
		 and age=#age#
		@}
		@if(!isEmpty(userName)){
		 and userName=#userName#
		@}
	 * 
	 * @param user
	 * @return
	 */
	public <T> List<T> selectByTemplement(T t) {
		
		SQLScript script = getScript(t.getClass(), SQLManager.SELECT_BY_TEMPLATE);
		Map<String, Object> param = MapKit.convertObjToMap(t);
		
		return (List<T>) script.select(t.getClass(), param);
	}
	
	/**
	 * 
	 * delete from user where 1=1 and id= #id#
	 * 
	 * 根据Id删除数据：支持联合主键
	 * @param clazz
	 * @param value
	 * @return
	 */
	public int deleteById(Class<?> clazz, Object ...value) {
		
		SQLScript script = getScript(clazz, SQLManager.DELETE_BY_ID);
		
		return script.deleteById(clazz, value);
	}
	
	/**
	 * 
	 * 需要处理","的问题，可能会出现update set user name=#name#, wehre 1=1 and ....的情况
	 * 
		update user set 
		@if(!isEmpty(name)){
			name=#name#,
		@}
		@if(!isEmpty(age)){
			age=#age#,
		@}
		@if(!isEmpty(userName)){
			userName=#userName#
		@} 
		 where 1=1 and id= #id# and name= #name#
	 * 
	 * @param obj
	 * @return
	 */
	public int updateById(Object obj){
		
		SQLScript script = getScript(obj.getClass(), SQLManager.UPDATE_BY_ID);
		
		return script.update(obj);
	}
	
	/**
	 * 
	 * 需要处理","的问题，可能会出现update set user name=#name#, wehre 1=1 and ....的情况
	 * 
	 * update user set 
		@if(!isEmpty(name)){
			name=#name#,
		@}
		@if(!isEmpty(id)){
			id=#id#,
		@}
		@if(!isEmpty(age)){
			age=#age#,
		@}
		@if(!isEmpty(userName)){
			userName=#userName#
		@} 
	 * @return
	 */
	public int updateAll(Class<?> clazz, Object param){
		
		SQLScript script = getScript(clazz, SQLManager.UPDATE_ALL);
		
		return script.update(param);
	}
	
	
	
	//OK - SELECT_BY_ID
	//OK - SELECT_ALL
	//OK - DELETE_BY_ID
	//OK - SELECT_BY_TEMPLATE
	//OK - UPDATE_BY_ID
	//UPDATE_ALL
	//INSERT
	//page
	//count
	
	
	
	//===============get/set===============

	public SQLLoader getSqlLoader() {
		return sqlLoader;
	}

	public void setSqlLoader(SQLLoader sqlLoader) {
		this.sqlLoader = sqlLoader;
	}

	public ConnectionSource getDs() {
		return ds;
	}

	public void setDs(ConnectionSource ds) {
		this.ds = ds;
	}
	
	public NameConversion getNc() {
		return nc;
	}

	public void setNc(NameConversion nc) {
		this.nc = nc;
		this.dbStyle.setNameConversion(nc);
	}

	public DBStyle getDbStyle() {
		return dbStyle;
	}

}
