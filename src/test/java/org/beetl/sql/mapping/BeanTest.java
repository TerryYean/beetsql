/**
 * 
 */
package org.beetl.sql.mapping;

import java.sql.Connection;
import java.sql.ResultSet;

import org.beetl.sql.core.UnderlinedNameConversion;
import org.beetl.sql.core.mapping.QueryMapping;
import org.beetl.sql.core.mapping.handler.BeanHandler;
import org.beetl.sql.pojo.User1;
import org.beetl.sql.pojo.User2;
import org.junit.Before;
import org.junit.Test;

/**
 * @author suxj
 *
 */
public class BeanTest {
	
	private DBBase base;
	private Connection conn;

	@Before
	public void setUp() throws Exception {
		
		base = DBBase.getInstance();
		conn = base.getConn();
	}

	@Test
	public void humQuery() {
		
		String sql = "select * from user1 where id=1";
		ResultSet rs = base.getRs(conn, sql);
		QueryMapping query = QueryMapping.getInstance();
		User1 user = query.query(rs, new BeanHandler<User1>(User1.class));
		System.out.println(user);
	}
	
	@Test
	public void humQuery2() {
		
	}
	
	@Test
	public void underLineQuery() {
		
		String sql = "select * from user2 where id=1";
		ResultSet rs = base.getRs(conn, sql);
		QueryMapping query = QueryMapping.getInstance();
		User2 user = query.query(rs, new BeanHandler<User2>(User2.class ,new UnderlinedNameConversion()));
		System.out.println(user);
	}
	
	@Test
	public void underLineQuery2() {
		
		String sql = "select * from user2";//多条记录默认取第一条
		ResultSet rs = base.getRs(conn, sql);
		QueryMapping query = QueryMapping.getInstance();
		User2 user = query.query(rs, new BeanHandler<User2>(User2.class ,new UnderlinedNameConversion()));
		System.out.println(user);
	}
	
	@Test
	public void underLineQuery3() {
		
		String sql = "select id ,t_age from user2 where id=1";//查询部分字段，其余为java类型默认值
		ResultSet rs = base.getRs(conn, sql);
		QueryMapping query = QueryMapping.getInstance();
		User2 user = query.query(rs, new BeanHandler<User2>(User2.class ,new UnderlinedNameConversion()));
		System.out.println(user);
	}

}
