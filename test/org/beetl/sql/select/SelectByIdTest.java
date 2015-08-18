/**
 * 
 */
package org.beetl.sql.select;

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
public class SelectByIdTest {

	private SQLLoader loader;
	private SQLManager manager;
	
	@Before
	public void before(){
		loader = new ClasspathLoader("/sql/mysql");
		manager = new SQLManager(new MySqlStyle(), loader, new MySqlConnectoinSource());
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
	
}
