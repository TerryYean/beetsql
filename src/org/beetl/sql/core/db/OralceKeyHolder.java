package org.beetl.sql.core.db;

import java.sql.Connection;

public class OralceKeyHolder implements SequenceKeyHolder {

	String seqName;
	Connection conn = null;
	public OralceKeyHolder(String seqName){
		this.seqName = seqName;
	}
	@Override
	public Object getKey() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setConnnection(Connection conn){
		this.conn = conn;
	}

}
