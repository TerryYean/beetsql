/**
 * 
 */
package org.beetl.sql.mapping;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

import org.beetl.sql.core.HumpNameConversion;
import org.beetl.sql.core.UnderlinedNameConversion;
import org.beetl.sql.core.mapping.QueryMapping;
import org.beetl.sql.core.mapping.handler.BeanListHandler;
import org.beetl.sql.pojo.User1;
import org.beetl.sql.pojo.User2;
import org.junit.Before;
import org.junit.Test;

/**
 * @author suxj
 *
 */
public class ListBeanTest {

	DBBase base;
	Connection conn;

	@Before
	public void setUp() throws Exception {
		
		base = DBBase.getInstance();
		conn = base.getConn();
	}

	@Test
	public void humQuery() {
		
		String sql = "select * from user1";
		ResultSet rs = base.getRs(conn, sql);
		QueryMapping query = QueryMapping.getInstance();
//		List<User1> list = query.query(rs, new BeanListHandler<User1>(User1.class));
		List<User1> list = query.query(rs, new BeanListHandler<User1>(User1.class ,new HumpNameConversion()));
		
		for(User1 user : list){
			System.out.println(user);
		}
	}
	
	@Test
	public void humQuery2() {
		
	}
	
	@Test
	public void underLineQuery() {
		
		String sql = "select * from user2";
		ResultSet rs = base.getRs(base.getConn(), sql);
		QueryMapping query = QueryMapping.getInstance();
		List<User2> list = query.query(rs, new BeanListHandler<User2>(User2.class ,new UnderlinedNameConversion()));
		for(User2 user : list){
			System.out.println(user);
		}
	}
	
	@Test
	public void underLineQuery2() {
		
		String sql = "select id ,t_age from user2";//查询部分字段，其余为java类型默认值
		ResultSet rs = base.getRs(base.getConn(), sql);
		QueryMapping query = QueryMapping.getInstance();
		List<User2> list = query.query(rs, new BeanListHandler<User2>(User2.class ,new UnderlinedNameConversion()));
		for(User2 user : list){
			System.out.println(user);
		}
	}

}
