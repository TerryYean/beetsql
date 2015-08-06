package org.beetl.sql.core;

import java.sql.Connection;

public interface ConnectionSource {
	
	public Connection getReadConn();
	public Connection getWriteConn();
}
