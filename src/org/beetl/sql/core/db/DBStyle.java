package org.beetl.sql.core.db;

import java.util.List;

import org.beetl.sql.core.SQLSource;

public interface DBStyle {
	public KeyHolder getKeyHolder(String name);
	public KeyHolder getKeyHolder();
	public SQLSource getInsert(Class c);
	public SQLSource getSelectById(Class c);
	public String getPageSQL(String sql);
	public List<Object> getPagePara(List<Object> paras,int start,int size);
}
