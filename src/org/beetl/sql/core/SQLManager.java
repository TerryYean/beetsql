package org.beetl.sql.core;

import java.util.Map;

public class SQLManager {
    private SQLLoader sqlLoader;
    ConnectionSource ds = null;
    public static final int  SELECT_ID = 0;
 
    public static final int  UPDATE_ALL = 1;
    public static final int  UPDATE_VALUE = 2;
    
	public SQLManager(SQLLoader loader,ConnectionSource ds){
		this.sqlLoader = loader;
	}
	
	protected SQLResult getSQLResult(String id,Map paras){
		SQLScript script = getScript(id);
		return  script.run(paras);
	}
	
	public SQLScript getScript(String id){
		String template = sqlLoader.getSQL(id).getTemplate();
		SQLScript script = new SQLScript(template,this);
		script.setId(id);
		return script;
	}
	
	public SQLScript getScript(Class cls,int flag){
		switch(flag){
		case SELECT_ID:{
			String template = sqlLoader.generationGetByid(cls).getTemplate();
			SQLScript script = new SQLScript(template,this);
			return script;
		}
		case UPDATE_VALUE:{
			String template  = sqlLoader.generationUpdate(cls).getTemplate();
			SQLScript script = new SQLScript(template,this);
			return script;
		}
		default:{
			throw new UnsupportedOperationException();
		}
		}
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
