package org.beetl.sql.pojo;

import org.beetl.sql.core.annotatoin.AutoID;

public class Role {
	int id ;
	String name ;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@AutoID
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "Role [id=" + id + ", name=" + name + "]";
	}
	
}
