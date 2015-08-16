/**
 * 
 */
package org.beetl.sql.buildsql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.beetl.sql.core.ClasspathLoader;
import org.beetl.sql.core.SQLLoader;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.db.MySqlStyle;
import org.beetl.sql.pojo.User;
import org.junit.Before;
import org.junit.Test;

/**
 * @author suxinjie
 *
 */
public class ExecSqlTest {

	private SQLLoader loader;
	private SQLManager manager;
	
	@Before
	public void before(){
		loader = ClasspathLoader.instance("/sql/mysql");
		manager = new SQLManager(new MySqlStyle(), loader, new MySqlConnectoinSource());
	}

	/**
	 * 如果条件不匹配，默认取select * from user where 1=1的第一条记录
	 * 
	 * selectUser
		===
	    select * from user where 1=1
	    @if(user.age==1){
	    and age = #user.age#
	    @}
	 */
	@Test
	public void selectBySqlId() {
		
		User user = new User();
		user.setAge(1);
		Map<String, Object> paras = new HashMap<String, Object>();
		paras.put("user", user);
		List<User> result = manager.selectBySqlId("user.selectUser", User.class, paras);
		System.out.println(result.get(0));
	}
	
	/**
	 * 推荐写法
	 * 
	 * 如果条件不匹配，默认取select * from user where 1=1的第一条记录
	 * 
	 * selectUser2
		===
	    select * from user where 1=1
	    @if(age==12){
	    and age = #age#
	    @}
	 */
	@Test
	public void selectBySqlId2() {
		
		Map<String, Object> paras = new HashMap<String, Object>();
		paras.put("age", 12);
		List<User> result = manager.selectBySqlId("user.selectUser2", User.class, paras);
		System.out.println(result.get(0));
	}
	
	@Test
	public void selectBySqlId3() {
		
		Map<String, Object> paras = new HashMap<String, Object>();
		paras.put("age", 12);
		List<Map> result = manager.selectBySqlId("user.selectUser2", Map.class, paras);
		System.out.println(result.get(0));
	}
	
	@Test
	public void selectById(){
		
		User result = manager.selectById(User.class, 4, 5);//4,5为联合主键值
		System.out.println(result);
	}
	
//	不支持该写法：
//	@Test
//	public void selectById2(){
//		
//		SQLScript script = manager.getScript(User.class, SQLManager.SELECT_BY_ID);
//	
//		Map<String, Object> para = new HashMap<String, Object>();
//		para.put("id", 3);
//		User result = (User) script.uniqueResult(para);
//		System.out.println(result);
//	}
	
	@Test
	public void selectAll(){
		
		List<User> userList = manager.selectAll(User.class);
		for(User user : userList){
			System.out.println(user);
		}
		
	}
	
	/**
	 * 联合主键
	 */
	@Test
	public void deleteById(){
		
		User user = new User();
		user.setId(4);
		int i = manager.deleteById(User.class, 4, 5);
		System.out.println(i);
		
	}
	
	/**
	 * TODO 注意：如果Pojo中int id；没有设置值，那么会默认认为id=0，
	 * 可能导致查不出数据，建议将int设置为Integer（如果没set值，为null）
	 */
	@Test
	public void selectByTemplement(){
		
		User user = new User();
		user.setAge(5);
		List<User> userList = manager.selectByTemplement(user);
		System.out.println(userList.size());
		
		for(User u : userList){
			System.out.println(u);
		}
	}
	
	@Test
	public void updateById(){
		
		User user = new User();
		user.setId(4);
		user.setName("14");
		user.setAge(100);
//		user.setUserName("sfjsfksjhfjkshfshdjfhsjkfhdshjk");
		
		int i = manager.updateById(user);
		System.out.println(i);
	}
	
	@Test
	public void updateAll(){
		
		User user = new User();
//		user.setId(4);
//		user.setName("14");
		user.setAge(11);
//		user.setUserName("aaa");
		
		int i = manager.updateAll(User.class, user);
		System.out.println(i);
	}
	
	

}
