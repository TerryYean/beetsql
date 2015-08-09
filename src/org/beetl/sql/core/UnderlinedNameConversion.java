package org.beetl.sql.core;

import org.beetl.sql.core.kit.StringKit;

/***
 *  下划线命名转换
 * 
 * @author Gavin
 *
 */
public class UnderlinedNameConversion extends NameConversion {
	
	public String getTableName(String className) {
		return StringKit.enCodeUnderlined(className);
	}
	
	public String getColName(String attrName) {
		return StringKit.enCodeUnderlined(attrName);
	}

	@Override
	public String getClassName(String tableName) {
		return StringKit.toUpperCaseFirstOne(StringKit.deCodeUnderlined(tableName));
	}

	@Override
	public String getPropertyName(String colName) {
		return StringKit.deCodeUnderlined(colName);
	}
}
