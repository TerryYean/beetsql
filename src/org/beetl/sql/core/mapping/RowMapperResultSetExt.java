/**
 * 
 */
package org.beetl.sql.core.mapping;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.beetl.sql.core.RowMapper;

/**
 * @author suxinjie
 * 
 * 处理RowMapper.handleResultSet中的逻辑
 * 可以根据resultset对mapRow中的内容进行操作
 * 可用于关联表扩展和单表的属性增删改
 *
 */
public class RowMapperResultSetExt<T> implements ResultSetExt<List<T>>{
	
	private RowMapper<T> rowMapper;
	
	public RowMapperResultSetExt(RowMapper<T> _rowMapper){
		this.rowMapper = _rowMapper;
	}

	@Override
	public List<T> handleResultSet(ResultSet rs) throws SQLException {
		int rowNum = 0;
		List<T> resultList = new ArrayList<T>();
		while(rs.next()){
			resultList.add(this.rowMapper.mapRow(rs, rowNum++));
		}
		return resultList;
	}
	

}
