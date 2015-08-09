package org.beetl.sql.core.db;

public class AutoIncKeyHolder implements AutoKeyHolder {

	public Object key = null;
	@Override
	public Object getKey() {
		return key;
	}
	
	public void setKey(Object key){
		this.key = key ;
	}
	
	

}
