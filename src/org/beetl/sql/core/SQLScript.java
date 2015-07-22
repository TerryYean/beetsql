package org.beetl.sql.core;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;

public class SQLScript {
	String sql;
	Map<String,Object> paras;
	String sqlId;
	String jdbcSql;
	List jdbcPara;
	public SQLScript(String sqlId,String sql,Map<String,Object> paras){
		this.sqlId = sqlId;
		this.sql = sql ;
		this.paras = paras;
	}
	
	public void run(){
		GroupTemplate gt = Beetl.instance().getGroupTemplate();
		Template t = gt.getTemplate(sql);
		jdbcPara = new LinkedList();
		for(Entry<String,Object> entry:paras.entrySet()){
			t.binding(entry.getKey(), entry.getValue());
		}
		t.binding("_paras",jdbcPara);
		
		jdbcSql = t.render();
	}
	
	public String getJdbcSQL(){
		return this.jdbcSql;
	}
	public List<Object> getJDBCParas(){
		return this.jdbcPara;
	}
}
