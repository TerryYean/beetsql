/**  
 * 文件名:    User1.java  
 * 描述:      
 * 作者:      suxj
 * 版本:      1.0  
 * 创建时间:  2015年8月9日 下午1:28:12  
 *  
 * 修改历史:  
 * 日期                          作者           版本         描述  
 * ------------------------------------------------------------------  
 * 2015年8月9日        suxj     1.0     1.0 Version  
 */
package org.beetl.sql.pojo;

/**
 * @ClassName: User1
 * @Description: TODO
 * @author: suxj
 * @date:2015年8月9日 下午1:28:12
 */
public class User1 {

	private int id; 		// int
	private String name; 	// varchar
	private int age; 		// int
	private String sex; 	// char
	private java.sql.Date createDate;//datatime
	private java.sql.Date deleteDate;//timestamp

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
	public java.sql.Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(java.sql.Date createDate) {
		this.createDate = createDate;
	}

	public java.sql.Date getDeleteDate() {
		return deleteDate;
	}

	public void setDeleteDate(java.sql.Date deleteDate) {
		this.deleteDate = deleteDate;
	}

	public String toString(){
		return "id="+id+",name="+name+",age="+age+",sex="+sex+",createDate="+createDate+",deleteDate="+deleteDate;
	}

}
