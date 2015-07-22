package org.beetl.sql.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {

	public static void main(String[] args) {
		testSimple();
//		testIf();
		

	}
	
	public static void testSimple(){
		String sql =" select * from user where id = ${user.id}";
		User user = new User();
		user.setId(12);
		Map paras = new HashMap();
		paras.put("user", user);
		SQLScript script = new SQLScript("selectUser",sql,paras);
		script.run();
		String jdbcSQL = script.getJdbcSQL();
		List jdbcParas = script.getJDBCParas();
		System.out.println(jdbcSQL);
		System.out.println(jdbcParas);
	}
	
	public static void testIf(){
		String sql =" select * from user where 1=1 \n"
				+"#if(user.name!=null){\n"
				+"and name=${user.name}\n"
				+"#}\n"
				+" and id = ${user.id}";
		User user = new User();
		user.setId(12);
		user.setName("xiandafu");
		Map paras = new HashMap();
		paras.put("user", user);
		SQLScript script = new SQLScript("selectUserByCondition",sql,paras);
		script.run();
		String jdbcSQL = script.getJdbcSQL();
		List jdbcParas = script.getJDBCParas();
		System.out.println(jdbcSQL);
		System.out.println(jdbcParas);
	}

	static class User{
		int id;
		String name;
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
		
	}
}
