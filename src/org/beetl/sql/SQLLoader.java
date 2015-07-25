package org.beetl.sql;

import org.beetl.sql.core.SQLSource;

public interface SQLLoader {
    public SQLSource getSQL(String id);
	public SQLSource generationUpdate(Class cls);
	public SQLSource generationGetByid(Class cls);
}
