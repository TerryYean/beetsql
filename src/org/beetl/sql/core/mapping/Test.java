package org.beetl.sql.core.mapping;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.beetl.sql.annotation.ID;

public class Test {
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		//mappingField(getConn());
		mappingObject(getConn());	    
	}
	
	public static void mappingObject(Connection conn){
		String sql = "select * from user";
		PreparedStatement pstmt;
	    try {
	        pstmt = (PreparedStatement)conn.prepareStatement(sql);
	        ResultSet rs = pstmt.executeQuery();
	        
	        List<User> list = Mapping.getInstance().mappingList(rs, User.class);
	        for(User user : list){
	        	System.out.println("===================");
	        	System.out.println("id="+user.getId()+",name="+user.getName()+",age="+user.getAge());
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public static void mappingField(Connection conn){
		String sql = "select count(*) from user";
		PreparedStatement pstmt;
	    try {
	        pstmt = (PreparedStatement)conn.prepareStatement(sql);
	        ResultSet rs = pstmt.executeQuery();
	        
	        Long count = Mapping.getInstance().mapping(rs, Long.class);
	        System.out.println("总记录条数 : "+count);
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
        String sql = "select * from users";
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
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
}


