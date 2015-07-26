package org.beetl.sql;

import org.beetl.sql.core.SQLScript;

public class SQLManager {
    private SQLLoader sqlLoader;
    public static final int  SELECT_ID = 0;
    public static final int  UPDATE_ALL = 1;
    public static final int  UPDATE_VALUE = 2;
    
	public SQLManager(SQLLoader loader){
		this.sqlLoader = loader;
	}
	
	public SQLScript getScript(String id){
		String template = sqlLoader.getSQL(id).getTemplate();
		SQLScript script = new SQLScript(template);
		return script;
	}
	
	public SQLScript getScript(Class cls,int flag){
		switch(flag){
		case SELECT_ID:{
			String template = sqlLoader.generationGetByid(cls).getTemplate();
			SQLScript script = new SQLScript(template);
			return script;
		}
		case UPDATE_VALUE:{
			String template  = sqlLoader.generationUpdate(cls).getTemplate();
			SQLScript script = new SQLScript(template);
			return script;
		}
		default:{
			throw new UnsupportedOperationException();
		}
		}
	}
	
	
	
	
}
