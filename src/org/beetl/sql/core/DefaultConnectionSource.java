package org.beetl.sql.core;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;

import javax.sql.DataSource;

class DefaultConnectionSource implements ConnectionSource{
	DataSource master = null;
	DataSource[] slaves = null;
	public DefaultConnectionSource(){
		
	}
	public DefaultConnectionSource(DataSource master,DataSource[] slaves){
		this.master = master;
		this.slaves = slaves;
		
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
	public DataSource getMaster() {
		return master;
	}
	public void setMaster(DataSource master) {
		this.master = master;
	}
	public DataSource[] getSlaves() {
		return slaves;
	}
	public void setSlaves(DataSource[] slaves) {
		this.slaves = slaves;
	}
	
	
}