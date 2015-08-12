package org.beetl.sql.core;

import org.beetl.sql.core.db.DBStyle;


public interface SQLLoader {
	public SQLSource getSQL(String id);
	public SQLSource getSelectByid(Class<?> cls);
	public SQLSource getSelectByTemplate(Class<?> cls);
	public SQLSource getDeleteByid(Class<?> cls);
	public SQLSource getSelectAll(Class<?> cls);
	public SQLSource getUpdataAll(Class<?> cls);
	public SQLSource getUpdataByid(Class<?> cls);
	public SQLSource getInsert(Class<?> cls);
	public DBStyle getDbs();
	public void setDbs(DBStyle dbs);
	
	
}
