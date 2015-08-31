package org.beetl.sql.core;

import java.sql.ResultSet;
import java.sql.SQLException;

/** 用来映射额外属性，如一对一
 * @author xiandafu
 *
 */
public interface RowMapper<T> {
	
//	 穿Object不行，如果object为处理后的值，那么在用户实现的RowMapper中
//	 还是要再次处理，如果是List，还是要在循环。
//	 T mapRow(T obj,ResultSet rs) throws SQLException;
	 
	 T mapRow(Object o,ResultSet rs, int rowNum) throws SQLException;
}