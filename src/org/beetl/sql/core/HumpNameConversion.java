package org.beetl.sql.core;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.beetl.sql.core.kit.StringKit;

/***
 * 驼峰命名转换
 * 
 * @author Gavin
 *
 */
public class HumpNameConversion extends NameConversion {

	public HumpNameConversion(ConnectionSource ds) {
		super(ds);
	}

	
	public String getTableName(Class<?> cls) {
		String tableName = StringKit.toLowerCaseFirstOne(cls.getSimpleName());
		try {
			ResultSet rs = dbmd.getTables(null, "%", tableName, new String[] { "TABLE" });
			if (rs.next()) {
				return tableName;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getColName(Class<?> cls,String attrName) {
		String colName = StringKit.toLowerCaseFirstOne(attrName);
		String tableName = StringKit.toLowerCaseFirstOne(cls.getSimpleName());
		try {
			ResultSet rs = dbmd.getColumns(null, "%", tableName, colName);
			if (rs.next()) {
				return colName;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getClassName(String tableName) {
		return StringKit.toUpperCaseFirstOne(tableName);
	}

	@Override
	public String getPropertyName(Class<?> cls,String colName) {
		Field[] fields = cls.getDeclaredFields();
		for (Field field : fields) {
			if(field.getName().toLowerCase().equals(colName.toLowerCase())){
				return field.getName();
			}
		}
		return null;
	}


	@Override
	public List<String> getId(Class<?> cls) {
		String tableName = StringKit.toLowerCaseFirstOne(cls.getSimpleName());
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
