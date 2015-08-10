package org.beetl.sql.core.mapping;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.beetl.sql.core.mapping.handler.BeanHandler;
import org.beetl.sql.core.mapping.handler.BeanListHandler;
import org.beetl.sql.core.mapping.handler.MapHandler;
import org.beetl.sql.core.mapping.handler.MapListHandler;
import org.beetl.sql.core.mapping.handler.ScalarHandler;

public class Test {
	
	public static void main(String[] args) {
//		Bean query(rs ,BeanHandler<User>(User.class));
//		List<Bean> query(rs, BeanListHandler<User>(User.class));
//		Integer query(rs ,ScalarHandler<String>());
//		Map<String ,Object> query(rs ,MappingHandler());
//		List<Map<String ,Object>> query(rs ,MappingListHandler());
		
		
		mappingObject(getConn());	 
		//mappingObjectList(getConn());
		//mappingScalar(getConn());
		//mappingMap(getConn());
		//mappingMapList(getConn());
		//mappingMatchObject(getConn());
		
	}
	
	public static void mappingMatchObject(Connection conn){
		String sql = "select * from user where id =3";
		//String sql = "select id from user where id =3";
		PreparedStatement pstmt;
	    try {
	        pstmt = (PreparedStatement)conn.prepareStatement(sql);
	        ResultSet rs = pstmt.executeQuery();
	        
	        QueryMapping query = new QueryMapping();
	        String[][] arr = {{"id","tId"},{"name","tName"},{"age","tAge"}};//数据库字段：pojo字段
	        MatchUser user = query.query(rs, new BeanHandler<MatchUser>(MatchUser.class ,new BasicRowProcessor(new StrategyBeanProcessor(new ArrayMatcher(arr)))));
	        
        	System.out.println("===================");
        	System.out.println("id="+user.gettId()+",name="+user.gettName()+",age="+user.gettAge());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public static void mappingMapList(Connection conn){
		String sql = "select * from user";
		PreparedStatement pstmt;
	    try {
	        pstmt = (PreparedStatement)conn.prepareStatement(sql);
	        ResultSet rs = pstmt.executeQuery();
	        
	        QueryMapping query = new QueryMapping();
	        List<Map<String ,Object>> list = query.query(rs, new MapListHandler()); 
	        
        	for(Map<String ,Object> map : list){
        		System.out.println("==================");
        		for(Map.Entry<String, Object> entry : map.entrySet()){
            		System.out.println(entry.getKey()+"="+entry.getValue());
            	}
        	}
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public static void mappingMap(Connection conn){
		String sql = "select * from user where id=3";
		PreparedStatement pstmt;
	    try {
	        pstmt = (PreparedStatement)conn.prepareStatement(sql);
	        ResultSet rs = pstmt.executeQuery();
	        
	        QueryMapping query = new QueryMapping();
	        Map<String ,Object> map = query.query(rs, new MapHandler()); 
	        
        	for(Map.Entry<String, Object> entry : map.entrySet()){
        		System.out.println(entry.getKey()+"="+entry.getValue());
        	}
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public static void mappingScalar(Connection conn){
		//String sql = "select name from user where id = 2";
		//String sql = "select count(*) from user";
		String sql = "select name ,id ,age ,sex from user";
		PreparedStatement pstmt;
	    try {
	        pstmt = (PreparedStatement)conn.prepareStatement(sql);
	        ResultSet rs = pstmt.executeQuery();
	        
	        QueryMapping query = new QueryMapping();
//	        String name = query.query(rs, new ScalarHandler<String>());  
//	        Long name = query.query(rs, new ScalarHandler<Long>());
//	        String name = query.query(rs, new ScalarHandler<String>()); 
//	        Integer name = query.query(rs, new ScalarHandler<Integer>("id")); 
//	        Integer name = query.query(rs, new ScalarHandler<Integer>("age")); 
	        String name = query.query(rs, new ScalarHandler<String>(4)); 
	        
        	System.out.println("===================");
        	System.out.println(name);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public static void mappingObject(Connection conn){
		String sql = "select id ,name ,age ,sex from user where id =3";
		//String sql = "select id from user where id =3";
		PreparedStatement pstmt;
	    try {
	        pstmt = (PreparedStatement)conn.prepareStatement(sql);
	        ResultSet rs = pstmt.executeQuery();
	        
	        QueryMapping query = new QueryMapping();
	        User user = query.query(rs, new BeanHandler<User>(User.class));
	        
        	System.out.println("===================");
        	System.out.println("id="+user.getId()+",name="+user.getName()+",age="+user.getAge());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public static void mappingObjectList(Connection conn){
		String sql = "select id ,name ,age ,sex from user";
		//String sql = "select id from user";
		PreparedStatement pstmt;
	    try {
	        pstmt = (PreparedStatement)conn.prepareStatement(sql);
	        ResultSet rs = pstmt.executeQuery();
	        
	        QueryMapping query = new QueryMapping();
	        List<User> list = query.query(rs, new BeanListHandler<User>(User.class));  
	        
	        for(User user : list){
	        	System.out.println("===================");
	        	System.out.println("id="+user.getId()+",name="+user.getName()+",age="+user.getAge());
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public static Connection getConn() {
		String driver = "com.mysql.jdbc.Driver";
        String dbName = "test";
        String passwrod = "123456";
        String userName = "sue";
        String url = "jdbc:mysql://localhost:3306/" + dbName;
        Connection conn = null;
        try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, userName,passwrod);
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

class User{
	int id;
	String name;
	int age;
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
	
}

class MatchUser {
	int tId;
	String tName;
	int tAge;

	public int gettId() {
		return tId;
	}

	public void settId(int tId) {
		this.tId = tId;
	}

	public String gettName() {
		return tName;
	}

	public void settName(String tName) {
		this.tName = tName;
	}

	public int gettAge() {
		return tAge;
	}

	public void settAge(int tAge) {
		this.tAge = tAge;
	}

}

