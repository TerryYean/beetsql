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
import org.beetl.sql.core.SQLScript;
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
	public void selectById() {
		
		SQLScript script = manager.getScript("user.selectUser");
		
		User user = new User();
		user.setAge(1);
		Map<String, Object> paras = new HashMap<String, Object>();
		paras.put("user", user);
		List<User> result = script.select(paras, User.class);
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
	public void selectById2() {
		
		SQLScript script = manager.getScript("user.selectUser2");
		
		Map<String, Object> paras = new HashMap<String, Object>();
		paras.put("age", 12);
		List<User> result = script.select(paras, User.class);
		System.out.println(result.get(0));
	}
	
	@Test
	public void selectById3(){
		
		SQLScript script = manager.getScript(User.class, SQLManager.SELECT_BY_ID);
	
		User user = new User();
		user.setId(3);
		User result = script.unique(user);
		System.out.println(result);
	}
	
//	不支持该写法：select-by-id感觉api需要改进，用户体验不好
//	@Test
//	public void selectById4(){
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
		
		SQLScript script = manager.getScript(User.class, SQLManager.SELECT_ALL);
		List<User> userList = script.select(null, User.class);
		for(User user : userList){
			System.out.println(user);
		}
		
	}
	
	@Test
	public void deleteById(){
		
		SQLScript script = manager.getScript(User.class, SQLManager.DELETE_BY_ID);
//		script.deleteById()
		
	}

}
