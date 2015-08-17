package org.beetl.sql.core.db;

import java.lang.reflect.Method;
import java.util.List;

import org.beetl.sql.core.NameConversion;
import org.beetl.sql.core.SQLSource;
/**
 * 用来描述数据库差异，主键生成，sql语句，翻页等
 * @author xiandafu
 *
 */
public interface DBStyle {

	public static final int ID_ASSIGN = 1 ;
	public static final int ID_AUTO = 2 ;
	public static final int ID_SEQ = 3 ;
	
	
	public SQLSource getSelectById(Class<?> c);
	public String getPageSQL(String sql);
	public List<Object> getPagePara(List<Object> paras,int start,int size);
	
	public SQLSource generationSelectByid(Class<?> cls);
	public SQLSource generationSelectByTemplate(Class<?> cls);
	public SQLSource generationSelectCountByTemplate(Class<?> cls);
	public SQLSource generationDeleteByid(Class<?> cls);
	public SQLSource generationSelectAll(Class<?> cls);
	public SQLSource generationUpdateAll(Class<?> cls);
	public SQLSource generationUpdateByid(Class<?> cls);
	public SQLSource generationUpdateTemplate(Class<?> cls);
	public SQLSource generationBatchUpdateByid(Class<?> cls);
	public SQLSource generationInsert(Class<?> cls);
	
	public int getIdType(Method idMethod);
	
	
	public NameConversion getNameConversion();
	public MetadataManager getMetadataManager();
	public void setNameConversion(NameConversion nameConversion);
	public void setMetadataManager(MetadataManager metadataManager);
}
