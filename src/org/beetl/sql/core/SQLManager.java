package org.beetl.sql.core;

import java.util.Map;

import org.beetl.sql.core.db.DBStyle;
import org.beetl.sql.core.db.MetadataManager;

public class SQLManager {
	private DBStyle dbStyle;
	private SQLLoader sqlLoader;
	ConnectionSource ds = null;//数据库连接管理
	NameConversion nc = null;//名字转换器
	Interceptor[] inters = {};
	public static final int SELECT_BY_ID = 0;
	public static final int SELECT_BY_TEMPLATE = 1;
	public static final int DELETE_BY_ID = 2;
	public static final int SELECT_ALL = 3;
	public static final int UPDATE_ALL = 4;
	public static final int UPDATE_BY_ID = 5;
	public static final int INSERT = 6;

	public SQLManager() {
		// for framework ,sprint .etc
	}

	public SQLManager(DBStyle dbStyle, SQLLoader loader, ConnectionSource ds) {
		this.dbStyle = dbStyle;
		this.sqlLoader = loader;
		this.ds = ds;
		this.nc = new HumpNameConversion();
		this.dbStyle.setNameConversion(nc);
		this.dbStyle.setMetadataManager(new MetadataManager(ds));
		this.sqlLoader.setDbs(dbStyle);
	}

	public SQLManager(DBStyle dbStyle, SQLLoader sqlLoader,
			ConnectionSource ds, NameConversion nc,
			MetadataManager metadataManager) {
		this.dbStyle = dbStyle;
		this.sqlLoader = sqlLoader;
		this.ds = ds;
		this.nc = nc;
		this.dbStyle.setNameConversion(nc);
		this.dbStyle.setMetadataManager(metadataManager);
		this.sqlLoader.setDbs(dbStyle);
	}

	public SQLManager(DBStyle dbStyle, SQLLoader sqlLoader,
			ConnectionSource ds, NameConversion nc, Interceptor[] inters) {
		this.dbStyle = dbStyle;
		this.sqlLoader = sqlLoader;
		this.ds = ds;
		this.nc = nc;
		this.inters = inters;
	}

	public NameConversion getNc() {
		return nc;
	}

	public void setNc(NameConversion nc) {
		this.nc = nc;
		this.dbStyle.setNameConversion(nc);
	}

	protected SQLResult getSQLResult(String id, Map<String, Object> paras) {
		SQLScript script = getScript(id);
		return script.run(paras);
	}

	public SQLScript getScript(String id) {
		String template = sqlLoader.getSQL(id).getTemplate();
		SQLScript script = new SQLScript(template, this);
		script.setId(id);
		return script;
	}

	public SQLScript getScript(Class<?> cls, int flag) {
		switch (flag) {
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
			String template = sqlLoader.getInsert(cls).getTemplate();
			SQLScript script = new SQLScript(template, this);
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

}
