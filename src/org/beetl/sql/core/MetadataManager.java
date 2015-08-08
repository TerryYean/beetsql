package org.beetl.sql.core;

import java.lang.reflect.Field;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MetadataManager {
	
	private ConnectionSource ds = null;
	private DatabaseMetaData dbmd = null;
	
	public MetadataManager(ConnectionSource ds) {
		super();
		this.ds = ds;
		try {
			this.dbmd = ds.getReadConn(null).getMetaData(); 
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ConnectionSource getDs() {
		return ds;
	}

	public void setDs(ConnectionSource ds) {
		this.ds = ds;
	}
	/***
	 * 表是否在数据库中
	 * @param tableName
	 * @return
	 */
	public boolean existtable(String tableName) {
		try {
			ResultSet rs = dbmd.getTables(null, "%", tableName, new String[] { "TABLE" });
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	/****
	 * 字段是否在表中
	 * @param tableName
	 * @param colName
	 * @return
	 */
	public boolean existColName(String tableName,String colName) {
		try {
			ResultSet rs = dbmd.getColumns(null, "%", tableName, colName);
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	/***
	 * 
	 * @param cls
	 * @param colName
	 * @return
	 */
	public boolean existPropertyName(Class<?> cls,String fieldName) {
		Field[] fields = cls.getDeclaredFields();
		for (Field field : fields) {
			if(field.getName().equals(fieldName)){
				return true;
			}
		}
		return false;
	}
	/***
	 * 获取表中的id列表
	 * @param tableName
	 * @return
	 */
	public List<String> getIds(String tableName) {
		List<String> idList = new ArrayList<String>();
		try {
			ResultSet rs =  dbmd.getPrimaryKeys(null,"%",tableName);
			while(rs.next()){
				idList.add(rs.getString("COLUMN_NAME"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(idList.size() < 1){
			return null;
		}
		return idList;
	}
}
