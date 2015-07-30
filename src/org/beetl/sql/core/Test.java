package org.beetl.sql.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.beetl.sql.annotation.ID;

public class Test {
	static MySqlConnectoinSource ds = new MySqlConnectoinSource();
    public static void main(String[] args) {
//    	testSimple();
//		testIf();
//    		testManager();
    	testUse();
	}
    
    public static void testManager(){
    	SQLLoader loader = new ClasspathLoader("/sql/mysql");
		SQLManager manager = new SQLManager(loader,ds);
		SQLScript script = manager.getScript("user.selectUser");
		Map paras = getUserParas();
		User result = (User)script.singleSelect(paras, User.class);
		
		SQLScript script2 = manager.getScript(User.class,SQLManager.SELECT_ID);
		User u = (User) script2.getById(result);//默认返回的是user.getById
		printUser(u);
		
		SQLScript script3 = manager.getScript(User.class,SQLManager.UPDATE_VALUE);//已经在30行生成了update语句
		System.out.println("sql === "+script3.sql);
		result.setName("xxxx");
		script3.update( result);
		
    }
    //便于测试
	public static void printUser(User user){
		System.out.println("user:{id:"+user.getId()+",name:"+user.getName()+"}");
	}
	
	public static void testUse(){
		SQLLoader loader = new ClasspathLoader("/sql/mysql");
		SQLManager manager = new SQLManager(loader,ds);
		SQLScript script = manager.getScript("user.selectByExample");
		User user = (User)script.singleSelect(new User(), User.class);
		// 
		
	}
	
	public static void testSimple(){
		SQLLoader loader = new ClasspathLoader("/sql/mysql");
		SQLManager manager = new SQLManager(loader,ds);
		String sql =" select * from user where id = ${id}";
		
		SQLScript script = new SQLScript(sql,manager);
		//一下方法需要完成
		User user = new User();
		user.setId(13);
		User result = (User)script.singleSelect(user, User.class);
		// 
		
	}
	
	static class MySqlConnectoinSource implements ConnectionSource{

		@Override
		public Connection getConn() {
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
		
	}
	
	
	
	private static Map getUserParas(){
		User user = new User();
		user.setId(2);
		Map paras = new HashMap();
		paras.put("user", user);
		return paras;
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
		@ID
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
