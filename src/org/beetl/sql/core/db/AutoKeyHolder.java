package org.beetl.sql.core.db;
/**
 * 列自动增长
 * @author xiandafu
 *
 */
public interface  AutoKeyHolder extends KeyHolder {
	/*PreparedStatement is better? */
	public void setKey(Object key);
}
