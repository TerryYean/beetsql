package org.beetl.sql.core.db;

import java.util.List;

/**
 * 没有什么
 * @author xiandafu
 *
 */
public class MySqlStyle extends AbstractDBStyle {
	
	private String pageNumber = "pageNumber";
	private String pageSize = "pageSize";

	@Override
	public String getPageSQL(String sql) {
		return sql+" limit " + HOLDER_START + pageNumber + HOLDER_END + " , " + HOLDER_START + pageSize + HOLDER_END;
	}

	@Override
	public List<Object> getPagePara(List<Object> paras, int start, int size) {
		paras.add(start);
		paras.add(size);
		return paras;
	}

	@Override
	public AbstractDBStyle instance() {
		if(adbs == null){
			adbs = new MySqlStyle();
		}
		return adbs;
	}

	public MySqlStyle() {
		super();
	}
}
