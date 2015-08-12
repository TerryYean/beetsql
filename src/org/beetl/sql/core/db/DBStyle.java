package org.beetl.sql.core.db;

import java.util.List;

import org.beetl.sql.core.NameConversion;
import org.beetl.sql.core.SQLSource;
/**
 * 用来描述数据库差异，主键生成，sql语句，翻页等
 * @author xiandafu
 *
 */
public interface DBStyle {
	public KeyHolder getKeyHolder(String name);
	public KeyHolder getKeyHolder();
	public SQLSource getInsert(Class c);
	public SQLSource getSelectById(Class c);
	public String getPageSQL(String sql);
	public List<Object> getPagePara(List<Object> paras,int start,int size);
	
	public SQLSource generationSelectByid(Class<?> cls);
	public SQLSource generationSelectByTemplate(Class<?> cls);
	public SQLSource generationDeleteByid(Class<?> cls);
	public SQLSource generationSelectAll(Class<?> cls);
	public SQLSource generationUpdataAll(Class<?> cls);
	public SQLSource generationUpdataByid(Class<?> cls);
	public SQLSource generationInsert(Class<?> cls);
	
	public NameConversion getNameConversion();
	public void setNameConversion(NameConversion nameConversion);
	public MetadataManager getMetadataManager();
	public void setMetadataManager(MetadataManager metadataManager);
}
