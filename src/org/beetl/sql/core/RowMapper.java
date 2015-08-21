package org.beetl.sql.core;

import java.sql.ResultSet;
import java.sql.SQLException;

/** 用来映射额外属性，如一对一
 * @author xiandafu
 *
 */
public interface RowMapper {
	 public void mapRow(Object obj,ResultSet rs, int rowNum,RowMapperContext ctx) throws SQLException;
}

//mapRow()参数适当减几个，晚上回家研究
//写一个RowMap处理类供后端调用mapRow方法
//提交代码回家搞起