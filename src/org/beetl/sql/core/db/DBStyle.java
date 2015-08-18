package org.beetl.sql.core.db;

import java.lang.reflect.Method;
import java.util.List;

import org.beetl.sql.core.NameConversion;
import org.beetl.sql.core.SQLSource;
import org.beetl.sql.core.engine.Beetl;
/**
 * 用来描述数据库差异，主键生成，sql语句，翻页等
 * @author xiandafu
 *
 */
public interface DBStyle {

	public static final int ID_ASSIGN = 1 ;
	public static final int ID_AUTO = 2 ;
	public static final int ID_SEQ = 3 ;
	
	public String getPageSQL(String sql);
	public List<Object> getPagePara(List<Object> paras,int start,int size);
	
	public int getIdType(Method idMethod);
	
	public void init(Beetl beetl);
	
	public SQLSource genSelectById(Class<?> cls);
	public SQLSource genSelectByTemplate(Class<?> cls);
	public SQLSource genSelectCountByTemplate(Class<?> cls);
	public SQLSource genDeleteById(Class<?> cls);
	public SQLSource genSelectAll(Class<?> cls);
	public SQLSource genUpdateAll(Class<?> cls);
	public SQLSource genUpdateById(Class<?> cls);
	public SQLSource genUpdateTemplate(Class<?> cls);
	public SQLSource genBatchUpdateById(Class<?> cls);
	public SQLSource genInsert(Class<?> cls);
	
	public NameConversion getNameConversion();
	public MetadataManager getMetadataManager();
	public void setNameConversion(NameConversion nameConversion);
	public void setMetadataManager(MetadataManager metadataManager);
	
}
