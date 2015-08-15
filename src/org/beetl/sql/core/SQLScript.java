package org.beetl.sql.core;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.sql.core.db.KeyHolder;
import org.beetl.sql.core.engine.Beetl;
import org.beetl.sql.core.kit.PojoKit;
import org.beetl.sql.core.kit.StringKit;
import org.beetl.sql.core.mapping.QueryMapping;
import org.beetl.sql.core.mapping.handler.BeanHandler;

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
		for (Entry<String, Object> entry : paras.entrySet()) {
			t.binding(entry.getKey(), entry.getValue());
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
	
	public Object singleSelect(Object paras, Class<?> target) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("_root", paras);
		Object o = singleSelect(map,target);
		
		return o;
	}
	
	

	/**
	 * 查询，返回一个mapping类实例
	 * 
	 * @param conn
	 * @param paras
	 * @param mapping
	 * @return
	 */
	public <T> T singleSelect(Map<String, Object> paras, Class<T> clazz) {
		SQLResult result = run(paras);
		String sql = result.jdbcSql;
		List<Object> objs = result.jdbcPara;
		ResultSet rs = null;
		PreparedStatement ps = null;
		T model = null;
		InterceptorContext ctx = this.callInterceptorAsBefore(this.id,sql, objs);
		sql = ctx.getSql();
		objs = ctx.getParas();
		// 执行jdbc
		try {
			ps = sm.getDs().getReadConn(ctx).prepareStatement(sql);
			for (int i = 0; i < objs.size(); i++)
				ps.setObject(i + 1, objs.get(i));
			rs = ps.executeQuery();
//			model = getModel(rs, mapping);
			model = queryMapping.query(rs, new BeanHandler<T>(clazz, this.sm.nc));
			this.callInterceptorAsAfter(ctx);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				// if(conn != null)conn.close();由连接池来管理？
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return model;
	}

	public List<Object> select(Connection conn, Map<String, Object> paras,
			Class<?> mapping) {
		throw new UnsupportedOperationException();
	}

	public List<Object> select(Connection conn, Map<String, Object> paras,
			Class<?> mapping, long start, long end) {
		throw new UnsupportedOperationException();
	}

	

	/***
	 * 获取一个实例
	 * 
	 * @param rs
	 * @param mapping
	 * @return
	 */
	public Object getModel(ResultSet rs, Class<?> mapping) {
		Object model = null;
		try {
			model = mapping.newInstance();
			Method[] methods = mapping.getDeclaredMethods();
			try {
				if (rs.next()) {
					for (Method method : methods) {
						String methodName = method.getName();
						if (methodName.startsWith("set")) {
							String attrName = StringKit.toLowerCaseFirstOne(methodName
									.substring(3));
							method.invoke(model, rs.getObject(attrName));
						}
					}
				} else {
					return null;
				}
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return model;
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

	public Object uniqueResult( Object obj) {
//		Map<String, Object> paras = new HashMap<String, Object>();
//		String tableName = obj.getClass().getSimpleName().toLowerCase();
		String tableName = this.sm.nc.getTableName(obj.getClass().getSimpleName());
//		paras.put(tableName, obj);
		return singleSelect( obj, obj.getClass());
	}

	public String getFieldValue(Field field, Object obj)
			throws IllegalArgumentException, IllegalAccessException {
		if (field.getType().equals(java.sql.Date.class)
				|| field.getType().equals(java.util.Date.class)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.format(field.get(obj));
		}
		return field.get(obj).toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
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

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
	
}
