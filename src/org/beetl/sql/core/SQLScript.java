package org.beetl.sql.core;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;

public class SQLScript {
    String sql;
	String jdbcSql;

	public SQLScript(String sql) {
		this.sql = sql;

	}

	private SQLResult run(Map<String, Object> paras) {
		GroupTemplate gt = Beetl.instance().getGroupTemplate();
		Template t = gt.getTemplate(sql);
		List jdbcPara = new LinkedList();
		for (Entry<String, Object> entry : paras.entrySet()) {
			t.binding(entry.getKey(), entry.getValue());
		}
		t.binding("_paras", jdbcPara);

		String jdbcSql = t.render();
		SQLResult result = new SQLResult();
		result.jdbcSql = jdbcSql;
		result.jdbcPara = jdbcPara;
		return result;
	}

	/**
	 * 查询，返回一个mapping类实例
	 * 
	 * @param conn
	 * @param paras
	 * @param mapping
	 * @return
	 */
	public Object singleSelect(Connection conn, Map<String, Object> paras,
			Class mapping) {
		SQLResult result = run(paras);
		String sql = result.jdbcSql;
		List<Object> objs = result.jdbcPara;
		ResultSet rs = null;
		Statement stmt = null;
		PreparedStatement ps = null;
		Object model = null;
		// 执行jdbc
		try {
			stmt = conn.createStatement();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < objs.size(); i++)
				ps.setObject(i+1, objs.get(i));
			rs = ps.executeQuery();
			model = getModel(rs, mapping);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)rs.close();
				if (ps != null)ps.close();
				if (stmt != null)stmt.close();
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

	class SQLResult {
		String jdbcSql;
		List jdbcPara;
	}

	public Object getModel(ResultSet rs, Class mapping) {
		Object model = null;
		try {
			model = mapping.newInstance();
			Field[] fields = mapping.getDeclaredFields();
			try {
				while (rs.next()) {
					for (Field field : fields) {
						Class type = field.getType();
						field.setAccessible(true);
						field.set(model, rs.getObject(field.getName(), type));
					}
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
}
