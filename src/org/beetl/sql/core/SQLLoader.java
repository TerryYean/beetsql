package org.beetl.sql.core;

public interface SQLLoader {
	
	public SQLSource getSQL(String id);

	public SQLSource generationSelectByid(Class<?> cls);

	public SQLSource generationSelectByTemplate(Class<?> cls);

	public SQLSource generationDeleteByid(Class<?> cls);

	public SQLSource generationSelectAll(Class<?> cls);

	public SQLSource generationUpdataAll(Class<?> cls);

	public SQLSource generationUpdataByid(Class<?> cls);

	public SQLSource generationUpdataByTemplate(Class<?> cls);

	public void setNameConversion(NameConversion nc);

	public NameConversion getNameConversion();
	
}
