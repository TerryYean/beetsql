package org.beetl.sql.core;

public class SQLSource {
	
	private String id;
	private String template;
	private String seqName;
	private int idType;

	public SQLSource() {
	}

	public SQLSource(String id, String template) {
		this.id = id;
		this.template = template;
	}

	public SQLSource(String template) {

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
