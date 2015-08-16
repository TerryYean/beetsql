package org.beetl.sql.insert;

import java.util.List;

import org.beetl.sql.buildsql.MySqlConnectoinSource;
import org.beetl.sql.core.ClasspathLoader;
import org.beetl.sql.core.SQLLoader;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.SQLScript;
import org.beetl.sql.core.db.KeyHolder;
import org.beetl.sql.core.db.MySqlStyle;
import org.beetl.sql.pojo.Role;
import org.beetl.sql.pojo.User;
import org.junit.Before;
import org.junit.Test;

public class KeyTest {
	private SQLLoader loader;
	private SQLManager manager;

	@Before
	public void before() {
		loader = ClasspathLoader.instance("/sql/mysql");
		manager = new SQLManager(new MySqlStyle(), loader, new MySqlConnectoinSource());
	}

	@Test
	public void addRole() {

		Role role = new Role();
		role.setName("manager");
		SQLScript script = manager.getScript(Role.class, manager.INSERT);
		KeyHolder holder = new KeyHolder();
		script.insert(role, holder);
		System.out.println(holder.getKey());
		
	}
}
