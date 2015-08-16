package org.beetl.sql.core;

public class SQLSource {
	String template;
	int idType;
	String seqName ;
	public SQLSource(){
		
	}
	public SQLSource(String template){
		this.template = template;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public int getIdType() {
		return idType;
	}
	public void setIdType(int idType) {
		this.idType = idType;
	}
	public String getSeqName() {
		return seqName;
	}
	public void setSeqName(String seqName) {
		this.seqName = seqName;
	}
	
	
}
