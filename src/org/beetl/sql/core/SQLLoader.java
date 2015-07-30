package org.beetl.sql.core;

public interface SQLLoader {
    public SQLSource getSQL(String id);
	public SQLSource generationUpdate(Class cls);
	public SQLSource generationGetByid(Class cls);
}
