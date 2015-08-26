/**  
 * 文件名:    User2.java  
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
 * @ClassName: User2
 * @Description: TODO
 * @author: suxj
 * @date:2015年8月9日 下午1:28:12
 */
public class User2 {

	private int id; 		// int
	private String tName; 	// varchar
	private int tAge; 		// int
	private String tSex; 	// char
	private java.sql.Date createDate;//datatime
	private java.sql.Timestamp deleteDate;//timestamp
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String gettName() {
		return tName;
	}

	public void settName(String tName) {
		this.tName = tName;
	}

	public int gettAge() {
		return tAge;
	}

	public void settAge(int tAge) {
		this.tAge = tAge;
	}

	public String gettSex() {
		return tSex;
	}

	public void settSex(String tSex) {
		this.tSex = tSex;
	}
	
	public java.sql.Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(java.sql.Date createDate) {
		this.createDate = createDate;
	}

	public java.sql.Timestamp getDeleteDate() {
		return deleteDate;
	}

	public void setDeleteDate(java.sql.Timestamp deleteDate) {
		this.deleteDate = deleteDate;
	}

	public String toString(){
		return "id="+id+",name="+tName+",age="+tAge+",sex="+tSex+",createDate="+createDate+",deleteDate="+deleteDate;
	}

}
