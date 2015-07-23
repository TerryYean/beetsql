package org.beetl.sql.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Test {

    public static void main(String[] args) {
		testSimple();
//		testIf();
		

	}
	
	public static void testSimple(){
		String sql =" select * from user where id = ${user.id}";
		User user = new User();
		user.setId(2);
		Map paras = new HashMap();
		paras.put("user", user);
		SQLScript script = new SQLScript(sql);
		//一下方法需要完成
		User result = (User)script.singleSelect(getConn(), paras, User.class);
		// 
		
	}
	
	private static Connection getConn(){
		String driver = "com.mysql.jdbc.Driver";
        String dbName = "test";
        String passwrod = "root";
        String userName = "root";
        String url = "jdbc:mysql://localhost:3306/" + dbName;
        String sql = "select * from users";
        Connection conn = null;
        try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, userName,
	                passwrod);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
	
//	public static void testIf(){
//		String sql =" select * from user where 1=1 \n"
//				+"#if(user.name!=null){\n"
//				+"and name=${user.name}\n"
//				+"#}\n"
//				+" and id = ${user.id}";
//		User user = new User();
//		user.setId(12);
//		user.setName("xiandafu");
//		Map paras = new HashMap();
//		paras.put("user", user);
//		SQLScript script = new SQLScript("selectUserByCondition",sql,paras);
//		script.run();
//		String jdbcSQL = script.getJdbcSQL();
//		List jdbcParas = script.getJDBCParas();
//		System.out.println(jdbcSQL);
//		System.out.println(jdbcParas);
//	}

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
