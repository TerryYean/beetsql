package org.beetl.sql.core;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.sql.core.db.KeyHolder;
import org.beetl.sql.core.db.MetadataManager;
import org.beetl.sql.core.engine.Beetl;
import org.beetl.sql.core.kit.MapKit;
import org.beetl.sql.core.mapping.QueryMapping;
import org.beetl.sql.core.mapping.handler.BeanHandler;
import org.beetl.sql.core.mapping.handler.BeanListHandler;
import org.beetl.sql.core.mapping.handler.MapListHandler;

public class SQLScript {
	
	SQLManager sm;
	String id ;
    String sql;
	String jdbcSql;
	QueryMapping queryMapping = QueryMapping.getInstance();

	public SQLScript(String sql,SQLManager sm) {
		this.sql = sql;
		this.sm = sm ;

	}

	protected SQLResult run(Map<String, Object> paras) {
		GroupTemplate gt = Beetl.instance().getGroupTemplate();
		Template t = gt.getTemplate(sql);
		List<Object> jdbcPara = new LinkedList<Object>();
		
		if(paras != null){
			for (Entry<String, Object> entry : paras.entrySet()) {
				t.binding(entry.getKey(), entry.getValue());
			}
		}
		
		t.binding("_paras", jdbcPara);
		t.binding("_manager", this.sm);
		t.binding("_id", id);

		String jdbcSql = t.render();
		SQLResult result = new SQLResult();
		result.jdbcSql = jdbcSql;
		result.jdbcPara = jdbcPara;
		return result;
	}
	
	public void insert(Object paras){
		
	}
	
	public void insert(Object paras,KeyHolder holder){
		
	}
	
	/**
	 * 查询是传入Pojo实体，直接绑定到_root。可以在sql中使用#age#，而无需#user.age#
	 * @param paras
	 * @param target
	 * @return
	 */
	public <T> T singleSelect(Object paras, Class<T> target) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("_root", paras);
		List<T> result = select(target, map);
		
		if(result.size() > 0){
			return result.get(0);
		}
		return null;
	}

	/**
	 * 查询,返回一个pojo集合
	 * 
	 * @param conn
	 * @param paras
	 * @param mapping
	 * @return
	 */
	public <T> List<T> select(Class<T> clazz, Map<String, Object> paras) {
		SQLResult result = run(paras);
		String sql = result.jdbcSql;
		List<Object> objs = result.jdbcPara;
		ResultSet rs = null;
		PreparedStatement ps = null;
		List<T> resultList = null;
		InterceptorContext ctx = this.callInterceptorAsBefore(this.id,sql, objs);
		sql = ctx.getSql();
		objs = ctx.getParas();
		try {
			ps = sm.getDs().getReadConn(ctx).prepareStatement(sql);
			for (int i = 0; i < objs.size(); i++)
				ps.setObject(i + 1, objs.get(i));
			rs = ps.executeQuery();
			
			if(clazz.isAssignableFrom(Map.class)){ //如果是Map的子类或者父类，返回List<Map<String,Object>>
				resultList = (List<T>) queryMapping.query(rs, new MapListHandler(this.sm.nc));
			}else{									//如果不是Map，理想成Pojo，返回List<pojo>
				resultList = queryMapping.query(rs, new BeanListHandler<T>(clazz, this.sm.nc));
			}
			
			this.callInterceptorAsAfter(ctx);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return resultList;
	}
	
	public List<Object> select(ConnectionSource conn, Map<String, Object> paras,
			Class<?> mapping) {
		throw new UnsupportedOperationException();
	}

	public List<Object> select(ConnectionSource conn, Map<String, Object> paras,
			Class<?> mapping, long start, long end) {
		throw new UnsupportedOperationException();
	}
	
	public int update(Object obj) {
		Map<String, Object> paras = new HashMap<String, Object>();
//		String tableName = obj.getClass().getSimpleName().toLowerCase();
//		paras.put(tableName, obj);
		paras.put("_root", obj);
		SQLResult result = run(paras);
		String sql = result.jdbcSql;
		List<Object> objs = result.jdbcPara;
		
		InterceptorContext ctx = this.callInterceptorAsBefore(this.id,sql, objs);
		sql = ctx.getSql();
		objs = ctx.getParas();
		int rs = 0;
		PreparedStatement ps = null;
		// 执行jdbc
		try {
			ps = sm.getDs().getWriteConn(ctx).prepareStatement(sql);
			for (int i = 0; i < objs.size(); i++)
				ps.setObject(i + 1, objs.get(i));
			rs = ps.executeUpdate();
			this.callInterceptorAsAfter(ctx);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return rs;
	}

	/**
	 * 查询单条记录
	 * @param obj
	 * @return
	 */
	public <T> T unique(Class<T> clazz, Object ...value) {
		
		MetadataManager mm = this.sm.getDbStyle().getMetadataManager();
		List<String> pkNames = mm.getIds(this.sm.getNc().getTableName(clazz.getSimpleName()));
		
		Map<String, Object> paras = MapKit.pksSetValue(pkNames, value);
		paras.put("_root", clazz);
		
		SQLResult result = run(paras);
		String sql = result.jdbcSql;
		List<Object> objs = result.jdbcPara;
		ResultSet rs = null;
		PreparedStatement ps = null;
		T model = null;
		InterceptorContext ctx = this.callInterceptorAsBefore(this.id,sql, objs);
		sql = ctx.getSql();
		objs = ctx.getParas();
		try {
			ps = sm.getDs().getReadConn(ctx).prepareStatement(sql);
			for (int i = 0; i < objs.size(); i++)
				ps.setObject(i + 1, objs.get(i));
			rs = ps.executeQuery();
			model = queryMapping.query(rs, new BeanHandler<T>(clazz, this.sm.nc));
			this.callInterceptorAsAfter(ctx);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return model;
	}

	/**
	 * 根据Id删除
	 * @param j 
	 * @param user
	 */
	public int deleteById(Class<?> clazz, Object ...value) {
		
		MetadataManager mm = this.sm.getDbStyle().getMetadataManager();
		List<String> pkNames = mm.getIds(this.sm.getNc().getTableName(clazz.getSimpleName()));
		
		Map<String, Object> paras = MapKit.pksSetValue(pkNames, value);
		paras.put("_root", clazz);
		
		SQLResult result = run(paras);
		String sql = result.jdbcSql;
		List<Object> objs = result.jdbcPara;
		
		InterceptorContext ctx = this.callInterceptorAsBefore(this.id,sql, objs);
		sql = ctx.getSql();
		objs = ctx.getParas();
		int rs = 0;
		PreparedStatement ps = null;
		try {
			ps = sm.getDs().getWriteConn(ctx).prepareStatement(sql);
			for (int i = 0; i < objs.size(); i++)
				ps.setObject(i + 1, objs.get(i));
			rs = ps.executeUpdate();
			this.callInterceptorAsAfter(ctx);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return rs;
	}
	
//	public String getFieldValue(Field field, Object obj)
//			throws IllegalArgumentException, IllegalAccessException {
//		if (field.getType().equals(java.sql.Date.class)
//				|| field.getType().equals(java.util.Date.class)) {
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			return sdf.format(field.get(obj));
//		}
//		return field.get(obj).toString();
//	}

	private InterceptorContext callInterceptorAsBefore(String sqlId,String sql,List<Object> paras){
		
		InterceptorContext ctx = new InterceptorContext(sqlId,sql,paras);
		for(Interceptor in:sm.inters){
			in.befor(ctx);
		}
		return ctx;
	}
	
	private void callInterceptorAsAfter(InterceptorContext ctx ){
		if(sm.inters==null) return  ;
		
		for(Interceptor in:sm.inters){
			in.befor(ctx);
		}
		return ;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
	
}
