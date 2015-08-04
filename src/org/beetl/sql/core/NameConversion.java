package org.beetl.sql.core;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

public abstract class NameConversion {
	/****
	 * 根据实体class获取表名
	 * @param cls
	 * @return
	 */
	public abstract String getTableName(Class<?> cls);
	/****
	 * 根据class和属性名，获取字段名，此字段必须存在表中，否则返回空
	 * @param cls
	 * @param attrName
	 * @return
	 */
	public abstract String getColName(Class<?> cls,String attrName);
	/****
	 * 根据tableName获取className
	 * @param clas
	 * @return
	 */
	public abstract String getClassName(String tableName);
	/****
	 * 根据class和colName获取属性名
	 * @param cls
	 * @param attrName
	 * @return
	 */
	public abstract String getPropertyName(Class<?> cls,String colName);
	/***
	 *  根据class获取表中所有的id
	 * @param cls
	 * @return
	 */
	public abstract List<String> getId(Class<?> cls);
	
	protected ConnectionSource ds = null;
	protected DatabaseMetaData dbmd = null;
	
	public ConnectionSource getDs() {
		return ds;
	}

	public void setDs(ConnectionSource ds) {
		this.ds = ds;
	}
	public NameConversion(ConnectionSource ds) {
		super();
		this.ds = ds;
		try {
			this.dbmd = ds.getConn().getMetaData(); 
		} catch (SQLException e) {
			// TODO: handle exception
		}
		
	}
	
}
