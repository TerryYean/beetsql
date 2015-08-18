package org.beetl.sql.core.db;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import org.beetl.sql.core.annotatoin.AssignID;
import org.beetl.sql.core.annotatoin.SeqID;

public class OracleStyle extends AbstractDBStyle {

	public OracleStyle() {
	}

	@Override
	public String getPageSQL(String sql) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object> getPagePara(List<Object> paras, int start, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getIdType(Method idMethod) {
		Annotation[] ans = idMethod.getAnnotations();
		int idType = DBStyle.ID_ASSIGN; // 默认是自增长

		for (Annotation an : ans) {
			if (an instanceof SeqID) {
				idType = DBStyle.ID_SEQ;
			} else if (an instanceof AssignID) {
				idType = DBStyle.ID_ASSIGN;
			}
		}

		return idType;

	}

}
