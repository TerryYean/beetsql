/**
 * 
 */
package org.beetl.sql.buildsql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import org.beetl.sql.DBConfig;
import org.beetl.sql.core.ConnectionSource;
import org.beetl.sql.core.InterceptorContext;

/**
 * @author suxinjie
 *
 */
public class MySqlConnectoinSource implements ConnectionSource {
	
	private Connection _getConn(){
		String driver = DBConfig.driver;
        String dbName = DBConfig.dbName;
        String password = DBConfig.password;
        String userName = DBConfig.userName;
        String url = DBConfig.url;
        Connection conn = null;
        try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, userName,
	                password);
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

	@Override
	public void onlyMasterBegin() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onlyMasterEnd() {
		// TODO Auto-generated method stub
		
	}

	

}
