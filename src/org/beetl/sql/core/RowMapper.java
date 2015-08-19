package org.beetl.sql.core;

import java.sql.ResultSet;

/** 用来映射额外属性，如一对一
 * @author xiandafu
 *
 */
public interface RowMapper {
	 public void mapRow(Object obj,ResultSet rs, int rowNum,RowMapperContext ctx);
}
