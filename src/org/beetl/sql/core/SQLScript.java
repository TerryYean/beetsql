package org.beetl.sql.core;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.sql.core.Beetl;

public class SQLScript {
	String sql;	
	String jdbcSql;
	
	public SQLScript(String sql){
		this.sql = sql ;
		
		
	}
	
	private SQLResult run(Map<String,Object> paras){
		GroupTemplate gt = Beetl.instance().getGroupTemplate();
		Template t = gt.getTemplate(sql);
		List jdbcPara = new LinkedList();
		for(Entry<String,Object> entry:paras.entrySet()){
			t.binding(entry.getKey(), entry.getValue());
		}
		t.binding("_paras",jdbcPara);
		
		String jdbcSql = t.render();
		SQLResult result = new SQLResult();
		result.jdbcSql = jdbcSql;
		result.jdbcPara = jdbcPara;
		return result ;
	}
	
	/** 查询，返回一个mapping类实例
	 * @param conn
	 * @param paras
	 * @param mapping
	 * @return
	 */
	public Object singleSelect(Connection conn,Map<String,Object> paras,Class mapping ){
		SQLResult result = run(paras);
		String sql = result.jdbcSql;
		List<Object> objs = result.jdbcPara;
		
		//执行jdbc 
		// PreparedStatment ps = conn.....
		//for(Object obj :objs) ps.setObject(i++,obj);
		//ResultSet rs = ps.executeQuery();
		//将rs 自动赋值给mapping类
		
		throw new UnsupportedOperationException("等你完成");
	}
	
	
	
	public List<Object> select(Connection conn,Map<String,Object> paras,Class mapping ){
		throw new UnsupportedOperationException();
	}
	
	public List<Object> select(Connection conn,Map<String,Object> paras,Class mapping,long start,long end ){
		throw new UnsupportedOperationException();
	}
	
	class SQLResult{
		String jdbcSql ;
		List jdbcPara ;
	}
	
}
