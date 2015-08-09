package org.beetl.sql.core;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;

import javax.sql.DataSource;

public class DataSourceHelper {
	public  static ConnectionSource getSingle(DataSource ds){
		return new DefaultConnectionSource(ds,null);
	}
	public  static ConnectionSource getMasterSlave(DataSource ds,DataSource[] slaves){
		return new DefaultConnectionSource(ds,slaves);
	}
}

class DefaultConnectionSource implements ConnectionSource{
	DataSource master = null;
	DataSource[] slaves = null;

	public DefaultConnectionSource(DataSource master,DataSource[] slaves){
		this.master = master;
		this.slaves = null;
		
	}
	@Override
	public Connection getReadConn(InterceptorContext ctx) {
		if(slaves==null||slaves.length==0) return getWriteConn(ctx);
		else{
			//随机，todo，换成顺序
			DataSource ds = slaves[new Random().nextInt(slaves.length)];
			try {
				return ds.getConnection();
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	@Override
	public Connection getWriteConn(InterceptorContext ctx) {
		try {
			return master.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
