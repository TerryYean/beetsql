package org.beetl.sql;

import org.beetl.sql.core.SQLScript;

public class SQLManager {
	private SQLLoader sqlLoader;
	public SQLManager(SQLLoader loader){
		this.sqlLoader = loader;
	}
	
	public SQLScript getScript(String id){
		String template = sqlLoader.getSQL(id);
		SQLScript script = new SQLScript(template);
		return script;
	}
	
	
}
