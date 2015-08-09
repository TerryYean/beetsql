package org.beetl.sql.core.db;

import org.beetl.sql.core.SQLResult;
import org.beetl.sql.core.SQLSource;
/**
 * 按照mysql来的，oralce需要重载insert，page方法
 * @author xiandafu
 *
 */
public abstract class AbstractDBStyle implements DBStyle {

	@Override
	/**
	 * 获得一个序列号获取，考虑到跨平台，提供一个name，对于mysql，总是忽略，对于oralce，是其序列号
	 */
	public KeyHolder getKeyHolder(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public KeyHolder getKeyHolder(){
		return null;
	}
	

	@Override
	public SQLSource getInsert(Class c) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLSource getSelectById(Class c) {
		// TODO Auto-generated method stub
		return null;
	}
	


}