package org.beetl.sql.core;

import java.util.List;

import org.beetl.core.Context;
import org.beetl.core.Function;

public class IncludeFunction implements Function {

	@Override
	public Object call(Object[] paras, Context ctx) {
		String id = (String)paras[0];
		SQLManager sm = (SQLManager) ctx.getGlobal("_manager");
		// 保留，免得被覆盖
		List list = (List)ctx.getGlobal("_paras");
		SQLResult result = sm.getSQLResult(id, ctx.globalVar);
		list.addAll(result.jdbcPara);
		ctx.set("_paras", list);
		return result.jdbcSql;
	}

}
