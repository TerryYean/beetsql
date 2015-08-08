package org.beetl.sql.core;

import org.beetl.sql.core.kit.StringKit;

/***
 * 驼峰命名转换
 * 
 * @author Gavin
 *
 */
public class HumpNameConversion extends NameConversion {
	
	public String getTableName(String className) {
		return StringKit.toLowerCaseFirstOne(className);
	}
	
	public String getColName(String attrName) {
		return StringKit.toLowerCaseFirstOne(attrName);
			}

	@Override
	public String getClassName(String tableName) {
		return StringKit.toUpperCaseFirstOne(tableName);
	}

	@Override
	public String getPropertyName(String colName) {
		return StringKit.toLowerCaseFirstOne(colName);
	}

}
