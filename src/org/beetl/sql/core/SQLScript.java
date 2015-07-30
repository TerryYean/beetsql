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

public class SQLScript {
	SQLManager sm;
    String sql;
	String jdbcSql;

	public SQLScript(String sql,SQLManager sm) {
		this.sql = sql;
		this.sm = sm ;

	}

	protected SQLResult run(Map<String, Object> paras) {
		GroupTemplate gt = Beetl.instance().getGroupTemplate();
		Template t = gt.getTemplate(sql);
		List jdbcPara = new LinkedList();
		for (Entry<String, Object> entry : paras.entrySet()) {
			t.binding(entry.getKey(), entry.getValue());
		}
		t.binding("_paras", jdbcPara);
		t.binding("_manager", this.sm);

		String jdbcSql = t.render();
		SQLResult result = new SQLResult();
		result.jdbcSql = jdbcSql;
		result.jdbcPara = jdbcPara;
		return result;
	}
	
	public Object singleSelect(Object paras,
			Class mapping) {
		Map map = new HashMap();
		map.put("_root", paras);
		SQLResult result = run(map);
		String sql = result.jdbcSql;
		List<Object> objs = result.jdbcPara;
		//todo:
		return null;
	}

	/**
	 * 查询，返回一个mapping类实例
	 * 
	 * @param conn
	 * @param paras
	 * @param mapping
	 * @return
	 */
	public Object singleSelect(Map<String, Object> paras,
			Class mapping) {
		SQLResult result = run(paras);
		String sql = result.jdbcSql;
		List<Object> objs = result.jdbcPara;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Object model = null;
		// 执行jdbc
		try {
			ps = sm.ds.getConn().prepareStatement(sql);
			for (int i = 0; i < objs.size(); i++)
				ps.setObject(i + 1, objs.get(i));
			rs = ps.executeQuery();
			model = getModel(rs, mapping);
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
			Class mapping) {
		throw new UnsupportedOperationException();
	}

	public List<Object> select(Connection conn, Map<String, Object> paras,
			Class mapping, long start, long end) {
		throw new UnsupportedOperationException();
	}

	

	/***
	 * 获取一个实例
	 * 
	 * @param rs
	 * @param mapping
	 * @return
	 */
	public Object getModel(ResultSet rs, Class mapping) {
		Object model = null;
		try {
			model = mapping.newInstance();
			Method[] methods = mapping.getDeclaredMethods();
			try {
				if (rs.next()) {
					for (Method method : methods) {
						String methodName = method.getName();
						if (methodName.startsWith("set")) {
							String attrName = toLowerCaseFirstOne(methodName
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
		String tableName = obj.getClass().getSimpleName().toLowerCase();
		paras.put(tableName, obj);
		SQLResult result = run(paras);
		String sql = result.jdbcSql;
		List<Object> objs = result.jdbcPara;
		int rs = 0;
		PreparedStatement ps = null;
		// 执行jdbc
		try {
			ps = sm.ds.getConn().prepareStatement(sql);
			for (int i = 0; i < objs.size(); i++)
				ps.setObject(i + 1, objs.get(i));
			rs = ps.executeUpdate();
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

	public Object getById( Object obj) {
		Map<String, Object> paras = new HashMap<String, Object>();
		String tableName = obj.getClass().getSimpleName().toLowerCase();
		paras.put(tableName, obj);
		return singleSelect( paras, obj.getClass());
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

	// 首字母转小写
	public String toLowerCaseFirstOne(String s) {
		if (Character.isLowerCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder())
					.append(Character.toLowerCase(s.charAt(0)))
					.append(s.substring(1)).toString();
	}

	// 首字母转大写
	public String toUpperCaseFirstOne(String s) {
		if (Character.isUpperCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder())
					.append(Character.toUpperCase(s.charAt(0)))
					.append(s.substring(1)).toString();
	}
}
