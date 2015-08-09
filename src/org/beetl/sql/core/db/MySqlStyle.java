package org.beetl.sql.core.db;

import java.util.List;

import org.beetl.sql.core.SQLResult;

/**
 * 没有什么
 * @author xiandafu
 *
 */
public class MySqlStyle extends AbstractDBStyle {
	
	public KeyHolder getKeyHolder(String name) {
		return this.getKeyHolder();
	}
	
	public KeyHolder getKeyHolder(){
		return new AutoIncKeyHolder();
	}


	@Override
	public String getPageSQL(String sql) {
		return sql+" offset ? limit ?";

	}

	@Override
	public List<Object> getPagePara(List<Object> paras, int start, int size) {
		paras.add(start);
		paras.add(size);
		return paras;
	}



}
