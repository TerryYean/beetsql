package org.beetl.sql.core;

public interface SQLLoader {
	public SQLSource getSQL(String id);
	public boolean isModified(String id);
	public boolean exist(String id);
	public void addSQL(String id,SQLSource msource);
	public boolean isAutoCheck();
	public void setAutoCheck(boolean check);

	
}
