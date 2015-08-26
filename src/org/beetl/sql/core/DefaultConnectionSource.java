package org.beetl.sql.core;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import javax.sql.DataSource;

public class DefaultConnectionSource implements ConnectionSource{
	DataSource master = null;
	DataSource[] slaves = null;
	public DefaultConnectionSource(){
		
	}
	public DefaultConnectionSource(DataSource master,DataSource[] slaves){
		this.master = master;
		this.slaves = slaves;
		
	}
	
	@Override
	public Connection getConn(String sqlId,boolean isUpdate,String sql,List paras){
		if(isUpdate) return this.getWriteConn(sqlId,sql,paras);
		else return this.getReadConn(sqlId, sql, paras);
	}
	
	@Override
	public Connection getMaster() {
		return this.doGetConnectoin(master);		
	}
	
	protected  Connection getReadConn(String sqlId,String sql,List paras) {
		if(slaves==null||slaves.length==0) return getWriteConn(sqlId,sql,paras);
		else{
		
			return nextSlaveConn();
		}
	}
	
	protected Connection getWriteConn(String sqlId,String sql,List paras) {
		
			return doGetConnectoin(master);
	
	}
	
	protected Connection nextSlaveConn(){
		//随机，todo，换成顺序
		DataSource ds = slaves[new Random().nextInt(slaves.length)];
		return doGetConnectoin(ds);
	}
	
	protected Connection doGetConnectoin(DataSource ds){
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			throw new BeetlSQLException(BeetlSQLException.CANNOT_GET_CONNECTION,e);
		}
	}
	public DataSource getMasterSource() {
		return master;
	}
	public void setMasterSource(DataSource master) {
		this.master = master;
	}
	public DataSource[] getSlaves() {
		return slaves;
	}
	public void setSlaves(DataSource[] slaves) {
		this.slaves = slaves;
	}
	
	
	
}