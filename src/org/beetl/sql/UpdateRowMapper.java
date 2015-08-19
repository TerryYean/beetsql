package org.beetl.sql;

import java.util.Map;

public interface UpdateRowMapper {
	public Map columnValue(Object target);
}
