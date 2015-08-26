package org.beetl.sql.core;

public class BeetlSQLException extends RuntimeException {
	
	public static final int 	CANNOT_GET_CONNECTION  = 0;
	public static final int 	SQL_EXCEPTION  = 1;
	public static final int 	CANNOT_GET_SQL  = 2;
	public static final int 	MAPPING_ERROR  = 3;
		
	int code ;
	
	public BeetlSQLException(int code){
		this.code = code;
	}
	
	public BeetlSQLException(int code,Exception e){
		super(e);
		this.code = code;
	}
	
	public BeetlSQLException(int code,String msg,Exception e){
		super(msg,e);
		this.code = code;
	}
	
	public BeetlSQLException(int code,String msg){
		super(msg);
		this.code = code;
	}
}
