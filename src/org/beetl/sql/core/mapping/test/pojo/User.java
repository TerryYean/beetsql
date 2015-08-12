/**  
 * 文件名:    User.java  
 * 描述:      
 * 作者:      suxj
 * 版本:      1.0  
 * 创建时间:  2015年8月12日 下午3:01:55  
 *  
 * 修改历史:  
 * 日期                          作者           版本         描述  
 * ------------------------------------------------------------------  
 * 2015年8月12日        suxj     1.0     1.0 Version  
 */
package org.beetl.sql.core.mapping.test.pojo;

/**
 * @ClassName: User
 * @Description: TODO
 * @author: suxj
 * @date:2015年8月12日 下午3:01:55
 */
public class User {
	int id;
	String name;
	int age;
	String userName;

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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
