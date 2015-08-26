package org.beetl.sql.ext;

import java.sql.Connection;

import javax.sql.DataSource;

import org.beetl.sql.core.BeetlSQLException;
import org.beetl.sql.core.DefaultConnectionSource;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.datasource.DataSourceUtils;

public class SpringConnectionSource extends  DefaultConnectionSource{
	protected Connection doGetConnectoin(DataSource ds) {
		try{
			return DataSourceUtils.getConnection(ds);
		}catch(CannotGetJdbcConnectionException ex){
	
			throw new BeetlSQLException(BeetlSQLException.CANNOT_GET_CONNECTION,ex);
		}
		
	}
	
}