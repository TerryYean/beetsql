/**
 * 
 */
package org.beetl.sql.select;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.beetl.sql.buildsql.MySqlConnectoinSource;
import org.beetl.sql.core.ClasspathLoader;
import org.beetl.sql.core.RowMapper;
import org.beetl.sql.core.SQLLoader;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.db.MySqlStyle;
import org.beetl.sql.pojo.Role;
import org.beetl.sql.pojo.User;
import org.junit.Before;
import org.junit.Test;
/**
 * @author suxinjie
 *
 */
public class PageSelectTest {

	private SQLLoader loader;
	private SQLManager manager;
	
	@Before
	public void before(){
		loader = new ClasspathLoader("/sql/mysql");
		manager = new SQLManager(new MySqlStyle(), loader, new MySqlConnectoinSource());
	}

	@Test
	public void selectPageByTemplate(){
		User user = new User();
		user.setAge(11);
		long total = manager.count(user);
		System.out.println(total);
		List<User> userList  = manager.selectByTemplate(user,1,2);
		
//		List<User> userList = manager.selectAll(User.class);
		for(User t : userList){
			System.out.println(t);
		}
		
	}
	
	@Test
	public void selectAll(){
		User user = new User();
		user.setAge(12);
		Integer total = manager.selectSingle("user.selectCountUser2", user, Integer.class);
		System.out.println(total);
		List<User> userList  = manager.select("user.selectUser2", User.class, user, 1,2);
		
//		List<User> userList = manager.selectAll(User.class);
		for(User t : userList){
			System.out.println(t);
		}
		
	}
	
	@Test
	public void selectAllOfRowMapper(){
		User user = new User();
		user.setAge(12);
		Integer total = manager.selectSingle("user.selectCountUser2", user, Integer.class);
		System.out.println(total);
		List<User> userList  = manager.select("user.selectCountUser2", User.class, user, new RowMapper<User>() {

			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				User u = new User();
				u.setId(rs.getInt(3));
				u.setName(rs.getString(4));
				Role r = manager.selectById(Role.class, rs.getInt(5));
				u.setRole(r);
				return u;
			}
		}, 1, 2);
		
//		List<User> userList = manager.selectAll(User.class);
		for(User t : userList){
			System.out.println(t);
		}
		
	}
	
}
