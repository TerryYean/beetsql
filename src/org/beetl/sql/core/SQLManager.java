package org.beetl.sql.core;

import static org.beetl.sql.core.kit.Constants.DELETE_BY_ID;
import static org.beetl.sql.core.kit.Constants.INSERT;
import static org.beetl.sql.core.kit.Constants.SELECT_COUNT_BY_TEMPLATE;
import static org.beetl.sql.core.kit.Constants.SELECT_ALL;
import static org.beetl.sql.core.kit.Constants.SELECT_BY_ID;
import static org.beetl.sql.core.kit.Constants.SELECT_BY_TEMPLATE;
import static org.beetl.sql.core.kit.Constants.UPDATE_ALL;
import static org.beetl.sql.core.kit.Constants.UPDATE_BY_ID;
import static org.beetl.sql.core.kit.Constants.classSQL;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.beetl.sql.core.db.DBStyle;
import org.beetl.sql.core.db.KeyHolder;
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
			case SELECT_COUNT_BY_TEMPLATE: {
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
				tempSource = this.dbStyle.genUpdateById(cls);
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
	

	/*============ 查询部分 ==================*/
	/**
	 * 通过sqlId进行查询,查询结果映射到clazz上
	 * @param sqlId
	 * @param clazz
	 * @param paras
	 * @return List<Pojo>
	 */
	public <T> List<T> select(String sqlId, Class<T> clazz, Map<String, Object> paras) { 
		
		return this.select(sqlId, clazz, paras, null);
	}
	/**
	 * 通过sqlId进行查询,查询结果映射到clazz上，mapper类可以定制映射
	 * @param sqlId
	 * @param clazz
	 * @param paras
	 * @param mapper
	 * @return
	 */
	public <T> List<T> select(String sqlId, Class<T> clazz, Map<String, Object> paras,RowMapper mapper) { 
		
		SQLScript script = getScript(sqlId);
		return script.select(clazz, paras,mapper);
	}
	
	/**
	 * 通过sqlId进行查询，查询结果映射到clazz上，输入条件是个Bean，Bean的属性可以被sql语句引用，如bean中有name属性,即方法getName,则sql语句可以包含
	 * name属性，如select * from xxx where name = #name#
	 * @param sqlId
	 * @param clazz
	 * @param paras
	 * @return List<Pojo>
	 */
	public <T> List<T> select(String sqlId, Class<T> clazz, Object paras) { 
		
		return this.select(sqlId, clazz, paras, null);
	}
	
	/**
	 * 通过sqlId进行查询:查询结果映射到clazz上，输入条件是个Bean,Bean的属性可以被sql语句引用，如bean中有name属性,即方法getName,则sql语句可以包含
	 * name属性，如select * from xxx where name = #name#。mapper类可以指定结果映射方式
	 * @param sqlId
	 * @param clazz
	 * @param paras
	 * @return List<Pojo>
	 */
	public <T> List<T> select(String sqlId, Class<T> clazz, Object paras,RowMapper mapper) { 
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("_root",paras);
		SQLScript script = getScript(sqlId);
		return script.select(clazz, param,mapper);
	}
	
	
	
	
	/** 翻页查询
	 * @param sqlId
	 * @param clazz
	 * @param paras
	 * @param start
	 * @param size
	 * @return
	 */
	public <T> List<T> select(String sqlId, Class<T> clazz, Object paras,int start,int size) { 
		
		return this.select(sqlId, clazz, paras, null, start, size);
	}
	
	public <T> List<T> select(String sqlId, Class<T> clazz, Object paras,RowMapper mapper,int start,int size) { 
		
		SQLScript script = getScript(sqlId);
		Map map = new HashMap();
		map.put("_root", paras);
		return script.select(map, clazz, mapper,start, size);
	}
	
	public <T> List<T> select(String sqlId, Class<T> clazz, Map paras,int start,int size) { 
		
		SQLScript script = getScript(sqlId);
		return script.select(paras, clazz, null,start, size);
	}
	
	
	public <T> List<T> select(String sqlId, Class<T> clazz, Map paras,RowMapper mapper,int start,int size) { 
		
		SQLScript script = getScript(sqlId);
		return script.select(paras, clazz, mapper,start, size);
	}
	
	
	/**
	 * btsql自动生成查询语句，查询clazz代表的表的所有数据。
	 * @param tempId
	 * @param clazz
	 * @param paras
	 * @return
	 */
	public <T> List<T> selectAll(Class<T> clazz) {

		SQLScript script = getScript(clazz, SELECT_ALL);
		return script.select(clazz, null);
	}
	
	
	public <T> List<T> selectAll(Class<T> clazz,RowMapper mapper) {

		SQLScript script = getScript(clazz, SELECT_ALL);
		return script.select(clazz, null,mapper);
	}
	
	/**
	 * 根据主键查询
	 * 获取唯一记录，如果纪录不存在，将会抛出异常
	 * @param clazz
	 * @param pkValues 主键 
	 * @return
	 */
	public <T> T selectById(Class<T> clazz,Object ...pkValues) {
		
		SQLScript script = getScript(clazz, SELECT_BY_ID);
		return script.unique(clazz, null,pkValues);
	}
	

	public <T> T selectById(Class<T> clazz, RowMapper mapper,Object ...pkValues) {
		
		SQLScript script = getScript(clazz, SELECT_BY_ID);
		return script.unique(clazz, mapper,pkValues);
	}
	
	/*=========模版查询===============*/
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
		return (List<T>) script.select(t.getClass(), param,null);
	}
	
	public <T> List<T> selectByTemplate(T t,RowMapper mapper) {
		
		SQLScript script = getScript(t.getClass(), SELECT_BY_TEMPLATE);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("_root",t);
		return (List<T>) script.select(t.getClass(), param,mapper);
	}
	
	
	
	

	
	public <T> List<T> selectByTemplate(T t,int start,int size) {
		
		return this.selectByTemplate(t, null, start, size);
	}
	
	public <T> List<T> selectByTemplate(T t,RowMapper mapper,int start,int size) {		
		
		SQLScript script = getScript(t.getClass(), SELECT_BY_TEMPLATE);
		SQLScript pageScript = this.getPageSqlScript(script.id);
		Map<String, Object> param = new HashMap<String, Object>();
		this.dbStyle.initPagePara(param, start, size);
		param.put("_root",t);
		
		return (List<T>) pageScript.select(t.getClass(), param,mapper);
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
	public <T> long count(T t) {
		
		SQLScript script = getScript(t.getClass(), SELECT_COUNT_BY_TEMPLATE);
		Long l = script.singleSelect(t, Long.class);
		return l;
	}
	
	
	public Long  selectLong(String id,Map paras) {
		return this.selectSingle(id, paras, Long.class);
	}
	
	public Long  selectLong(String id,Object paras) {
		return this.selectSingle(id, paras, Long.class);
	}
	
	public Integer  selectInt(String id,Object paras) {
		return this.selectSingle(id, paras, Integer.class);
	}
	
	public Integer  selectInt(String id,Map paras) {
		return this.selectSingle(id, paras, Integer.class);
	}
	
	public BigDecimal  selectBigDecimal(String id,Object paras) {
		return this.selectSingle(id, paras, BigDecimal.class);
	}
	
	public BigDecimal  selectBigDecimal(String id,Map paras) {
		return this.selectSingle(id, paras, BigDecimal.class);
	}
	
	public <T> T selectSingle(String id,Object paras, Class<T> target) {
		SQLScript script = getScript(id);
		return script.singleSelect(paras, target);
	}
	
	
	public <T> T selectSingle(String id,Map paras, Class<T> target) {
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
	
	//============= 插入 ===================  //
	
	public void insert(Class clazz,Object paras){
		SQLScript script = getScript(clazz,INSERT );
		script.insert(paras);
	}
	
	/** 插入，并获取主键
	 * @param clazz
	 * @param paras
	 * @param holder
	 */
	public void insert(Class clazz,Object paras,KeyHolder holder){
		SQLScript script = getScript(clazz,INSERT);
		script.insert(paras,holder );
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
	
	/****
	 * 批量更新
	 * @param list ,包含pojo（不支持map）
	 * @return
	 */
	public int[] updateByIdBatch(List<?> list){
		if(list == null || list.isEmpty()){
			return null;
		}
		SQLScript script = getScript(list.get(0).getClass(), UPDATE_BY_ID);
		return script.updateBatch(list);
	}
	
	
	/**  执行sql更新语句
	 * @param sqlId
	 * @param obj
	 * @return
	 */
	public int update(String sqlId,Object obj){
		SQLScript script = getScript(sqlId);
		return script.update(obj);
	}
	
	
	/**  执行sql更新语句
	 * @param sqlId
	 * @param paras
	 * @return
	 */
	public int update(String sqlId,Map paras){
		SQLScript script = getScript(sqlId);
		return script.update(paras);
	}
	
	
	/**  对pojo批量更新执行sql更新语句
	 * @param sqlId 
	 * @param paras 
	 * @return
	 */
	public int[] updateBatch(String sqlId,List list){
		SQLScript script = getScript(sqlId);
		return script.updateBatch(list);
	}
	
	/**批量更新
	 * @param sqlId
	 * @param maps  参数放在map里
	 * @return
	 */
	public int[] updateBatch(String sqlId,Map[] maps){
		SQLScript script = getScript(sqlId);
		return script.updateBatch(maps);
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

	public Beetl getBeetl() {
		return beetl;
	}

	
	
}
