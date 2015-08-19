package org.beetl.sql.core;

import java.util.Map;

/**
 * 增加或者更新的时候，传入UpdateRowMapper，可以更改sql语句，添加额外的更新，
 * 
 * 这只用于updateByTemplate,insert 里，用来处理or mapping
 * @author xiandafu
 *
 */
public interface UpdateRowMapper {
	/** 返回列名，和列值，update，insert语句更改原有sql，生成新的sql
	 * @param target
	 * @return
	 */
	public Map columnValue(Object target);
}
