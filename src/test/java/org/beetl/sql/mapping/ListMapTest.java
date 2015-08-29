/**
 * 
 */
package org.beetl.sql.mapping;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.beetl.sql.core.UnderlinedNameConversion;
import org.beetl.sql.core.mapping.QueryMapping;
import org.beetl.sql.core.mapping.handler.MapListHandler;
import org.junit.Before;
import org.junit.Test;

/**
 * @author suxj
 *
 */
public class ListMapTest {

	DBBase base;
	Connection conn;

	@Before
	public void setUp() throws Exception {
		
		base = DBBase.getInstance();
		conn = base.getConn();
	}

	@Test
	public void query() {
		
//		String sql = "select * from user1";
		String sql = "select * from user2";
		ResultSet rs = base.getRs(conn, sql);
		QueryMapping query = QueryMapping.getInstance();
		
//		List<Map<String ,Object>> list1 = query.query(rs, new MapListHandler());//断点查看
		List<Map<String ,Object>> list2 = query.query(rs, new MapListHandler(new UnderlinedNameConversion()));//断点查看
		
		//测试忽略key的大小写
		System.out.println(list2.get(0).get("tname"));
		System.out.println(list2.get(0).get("tNAME"));
	}
	
	@Test
	public void query2() {
		
	}

}
