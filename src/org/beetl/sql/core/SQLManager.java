package org.beetl.sql.core;

import static org.beetl.sql.core.kit.Constants.DELETE_BY_ID;
import static org.beetl.sql.core.kit.Constants.INSERT;
import static org.beetl.sql.core.kit.Constants.SELECT_ACCOUNT_BY_TEMPLATE;
import static org.beetl.sql.core.kit.Constants.SELECT_ALL;
import static org.beetl.sql.core.kit.Constants.SELECT_BY_ID;
import static org.beetl.sql.core.kit.Constants.SELECT_BY_TEMPLATE;
import static org.beetl.sql.core.kit.Constants.UPDATE_ALL;
import static org.beetl.sql.core.kit.Constants.UPDATE_BY_ID;
import static org.beetl.sql.core.kit.Constants.UPDATE_BY_ID_BATCH;
import static org.beetl.sql.core.kit.Constants.classSQL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.beetl.sql.core.db.DBStyle;
import org.beetl.sql.core.db.MetadataManager;
import org.beetl.sql.core.engine.Beetl;

public class SQLManager {
	
	private DBStyle dbStyle;
	private SQLLoader sqlLoader;
	private ConnectionSource ds = null;//数据库连接管理 TODO 应该指定一个默认数据库连接管理
	private NameConversion nc = null;//名字转换器
	Interceptor[] inters = {};
	Beetl beetl = null;

	public SQLManager(DBStyle dbStyle, SQLLoader sqlLoader, ConnectionSource ds) {
		beetl = new Beetl(sqlLoader);
		this.dbStyle = dbStyle;
		this.sqlLoader = sqlLoader;
		this.ds = ds;
		this.nc = new HumpNameConversion();
		this.dbStyle.setNameConversion(this.nc);
		this.dbStyle.setMetadataManager(new MetadataManager(this.ds));
		this.dbStyle.init(beetl);
		
	}
	
	public SQLManager(DBStyle dbStyle, SQLLoader sqlLoader,
			ConnectionSource ds, NameConversion nc, Interceptor[] inters) {
		beetl = new Beetl(sqlLoader);
		this.dbStyle = dbStyle;
		this.sqlLoader = sqlLoader;
		this.ds = ds;
		this.nc = nc;
		this.inters = inters;
		this.dbStyle.setNameConversion(this.nc);
		this.dbStyle.setMetadataManager(new MetadataManager(this.ds));
		this.dbStyle.init(beetl);
	}

	public SQLResult getSQLResult(String id, Map<String, Object> paras) {
		SQLScript script = getScript(id);
		return script.run(paras);
	}

	public SQLScript getScript(String id) {
		SQLSource source  = sqlLoader.getSQL(id);
		SQLScript script = new SQLScript(source, this);	
		return script;
	}

	/**
	 * 生成增删改查模板
	 * @param cls
	 * @param tempId
	 * @return
	 */
	public SQLScript getScript(Class<?> cls, int tempId) {
		String className = cls.getSimpleName().toLowerCase();
		String id = className +"."+ classSQL[tempId];
		SQLSource tempSource = this.sqlLoader.getSQL(id);
		if (tempSource != null) {
			return new SQLScript(tempSource,this);
		}
		switch (tempId) {
			case SELECT_BY_ID: {			
				tempSource = this.dbStyle.genSelectById(cls);
				break ;
			}
			case SELECT_BY_TEMPLATE: {
				tempSource = this.dbStyle.genSelectByTemplate(cls);
				break ;
			}
			case SELECT_ACCOUNT_BY_TEMPLATE: {
				tempSource = this.dbStyle.genSelectCountByTemplate(cls);
				break ;
			}
			case DELETE_BY_ID: {
				tempSource = this.dbStyle.genDeleteById(cls);
				break ;
			}
			case SELECT_ALL: {
				tempSource = this.dbStyle.genSelectAll(cls);
				break ;
			}
			case UPDATE_ALL: {
				tempSource = this.dbStyle.genUpdateAll(cls);
				break ;
			}
			case UPDATE_BY_ID: {
				tempSource = this.dbStyle.genBatchUpdateById(cls);
				break ;
			}
			case UPDATE_BY_ID_BATCH: {
				tempSource = this.dbStyle.genBatchUpdateById(cls);
				break ;
			}
			case INSERT: {
				tempSource = this.dbStyle.genInsert(cls);
				break ;
			}
			default: {
				throw new UnsupportedOperationException();
			}
		}
		
		tempSource.setId(id);
		sqlLoader.addSQL(id, tempSource);
		return new SQLScript(tempSource,this);
	}
	
	/****
	 * 获取为分页语句
	 * @param sql
	 * @return
	 */
	public SQLScript getPageSqlScript(String selectId) {
		String pageId = selectId+"-page";
		SQLSource source  = sqlLoader.getSQL(pageId);
		if(source!=null){
			return  new SQLScript(source, this);
		}
		SQLSource script = sqlLoader.getSQL(selectId); //TOOD 没猜错的话，应该会是个NullException,参数selectId是个模板语句
		String template = script.getTemplate();
		String pageTemplate = dbStyle.getPageSQL(template);
		source = new SQLSource(pageId,pageTemplate);
		sqlLoader.addSQL(pageId, source);
		return new SQLScript(source, this);
	}
	
//	/****
//	 * 获取总行数语句
//	 * @param sql
//	 * @return
//	 */
//	public SQLScript getCountSqlScript(String sql) {
//		if(!sql.toLowerCase().startsWith("select")){
//			throw new RuntimeException("这不是一个查询语句");
//		}
//		sql = "select count(*) "+sql.toLowerCase().substring(sql.indexOf("from"));
//		return new SQLScript(sql, this);
//	}
	
	/**
	 * 通过sqlId进行查询:查询自定义语句
	 * @param sqlId
	 * @param clazz
	 * @param paras
	 * @return List<Pojo>
	 */
	public <T> List<T> select(String sqlId, Class<T> clazz, Map<String, Object> paras) { 
		
		SQLScript script = getScript(sqlId);
		return script.select(clazz, paras);
	}
	
	public <T> List<T> select(String sqlId, Class<T> clazz, Map<String, Object> paras,RowMapper mapper) { 
		
		SQLScript script = getScript(sqlId);
		return script.select(clazz, paras);
	}
	
	/**
	 * 通过sqlId进行查询:查询自定义语句
	 * @param sqlId
	 * @param clazz
	 * @param paras
	 * @return List<Pojo>
	 */
	public <T> List<T> select(String sqlId, Class<T> clazz, Object paras) { 
		
		SQLScript script = getScript(sqlId);
		return script.select(clazz, paras);
	}
	
	
	public <T> List<T> select(String sqlId, Class<T> clazz, Object paras,int start,int size) { 
		
		SQLScript script = getScript(sqlId);
		return script.select(paras, clazz, start, size);
	}
	
	public <T> List<T> select(String sqlId, Class<T> clazz, Map paras,int start,int size) { 
		
		SQLScript script = getScript(sqlId);
		return script.select(paras, clazz, start, size);
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

		SQLScript script = getScript(clazz, SELECT_ALL);
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
		
		SQLScript script = getScript(clazz, SELECT_BY_ID);
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
	public <T> List<T> selectByTemplate(T t) {
		
		SQLScript script = getScript(t.getClass(), SELECT_BY_TEMPLATE);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("_root",t);
		return (List<T>) script.select(t.getClass(), param);
	}
	
	public <T> List<T> selectByTemplate(T t,int start,int size) {
		
	
		SQLScript script = getScript(t.getClass(), SELECT_BY_TEMPLATE);
		SQLScript pageScript = this.getPageSqlScript(script.id);
		Map<String, Object> param = new HashMap<String, Object>();
		this.dbStyle.initPagePara(param, start, size);
		param.put("_root",t);
		
		return (List<T>) pageScript.select(t.getClass(), param);
	}
	
	/**
	 * 查询总数
	 * @MethodName: selectCountByTemplate   
	 * @Description: TODO  
	 * @param @param t
	 * @param @return  
	 * @return long  
	 * @throws
	 */
	public <T> long selectCountByTemplate(T t) {
		
		SQLScript script = getScript(t.getClass(), SELECT_ACCOUNT_BY_TEMPLATE);
		Long l = script.singleSelect(t, Long.class);
		return l;
	}
	
	public <T> T singleSelect(String id,Object paras, Class<T> target) {
		SQLScript script = getScript(id);
		return script.singleSelect(paras, target);
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
		
		SQLScript script = getScript(clazz, DELETE_BY_ID);
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
		
		SQLScript script = getScript(obj.getClass(), UPDATE_BY_ID);
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
		
		SQLScript script = getScript(clazz, UPDATE_ALL);
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
