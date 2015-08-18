/**
 * 
 */
package org.beetl.sql.select;

import java.util.List;

import org.beetl.sql.buildsql.MySqlConnectoinSource;
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
public class SelectByTemplementTest {

	private SQLLoader loader;
	private SQLManager manager;
	
	@Before
	public void before(){
		loader = new ClasspathLoader("/sql/mysql");
		manager = new SQLManager(new MySqlStyle(), loader, new MySqlConnectoinSource());
	}

	/**
	 * TODO 注意：如果Pojo中int id；没有设置值，那么会默认认为id=0，
	 * 可能导致查不出数据，建议将int设置为Integer（如果没set值，为null）
	 */
	@Test
	public void selectByTemplement(){
		
		User user = new User();
		user.setAge(5);
		List<User> userList = manager.selectByTemplate(user);
		System.out.println(userList.size());
		
		for(User u : userList){
			System.out.println(u);
		}
	}
	
}
