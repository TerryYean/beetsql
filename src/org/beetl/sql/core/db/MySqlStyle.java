package org.beetl.sql.core.db;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import org.beetl.sql.core.annotatoin.AssignID;
import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.SeqID;
import org.beetl.sql.core.engine.Beetl;

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

	

	public MySqlStyle() {
	}

	@Override
	public int getIdType(Method idMethod) {
		Annotation[] ans = idMethod.getAnnotations();
		int  idType = DBStyle.ID_AUTO ; //默认是自增长
		
		for(Annotation an :ans){
			if(an instanceof AutoID){
				idType = DBStyle.ID_AUTO;
				continue ;
			}else if(an instanceof SeqID){
				//my sql not support 
			}else if(an instanceof AssignID){
				idType =DBStyle.ID_ASSIGN;
			}
		}
		
		return idType;
		
	}
}
