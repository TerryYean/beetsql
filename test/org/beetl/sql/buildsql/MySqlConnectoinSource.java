/**
 * 
 */
package org.beetl.sql.buildsql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import org.beetl.sql.core.ConnectionSource;
import org.beetl.sql.core.InterceptorContext;

/**
 * @author suxinjie
 *
 */
public class MySqlConnectoinSource implements ConnectionSource {
	
	private Connection _getConn(){
		String driver = "com.mysql.jdbc.Driver";
        String dbName = "test";
        String passwrod = "lijzh780214";
        String userName = "root";
        String url = "jdbc:mysql://127.0.0.1:3306/" + dbName;
        Connection conn = null;
        try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, userName,
	                passwrod);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	@Override
	public Connection getMaster() {
		return _getConn();
	}

	@Override
	public Connection getConn(String sqlId, boolean isUpdate, String sql, List paras) {
		return _getConn();
	}

	

}
