package org.beetl.sql.core;

import javax.sql.DataSource;

public class DataSourceHelper {
	public  static ConnectionSource getSingle(DataSource ds){
		return new DefaultConnectionSource(ds,null);
	}
	public  static ConnectionSource getMasterSlave(DataSource ds,DataSource[] slaves){
		return new DefaultConnectionSource(ds,slaves);
	}
}
