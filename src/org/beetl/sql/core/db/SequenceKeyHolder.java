package org.beetl.sql.core.db;

import java.sql.Connection;

public interface SequenceKeyHolder extends KeyHolder {
	public void setConnnection(Connection conn);
}
