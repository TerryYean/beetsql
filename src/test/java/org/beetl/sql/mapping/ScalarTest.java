/**
 * 
 */
package org.beetl.sql.mapping;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;

import org.beetl.sql.core.mapping.QueryMapping;
import org.beetl.sql.core.mapping.handler.ScalarHandler;
import org.junit.Before;
import org.junit.Test;

/**
 * http://dev.mysql.com/doc/connector-j/en/connector-j-reference-type-conversions.html
 * 
 * @author suxj
 *
 */
public class ScalarTest {

	DBBase base;
	Connection conn;

	@Before
	public void setUp() throws Exception {
		
		base = DBBase.getInstance();
		conn = base.getConn();
	}

	@Test
	public void query() {
		
		String sql = "select id from user1 where id=1";
		ResultSet rs = base.getRs(conn, sql);
		QueryMapping query = QueryMapping.getInstance();
		Integer id = query.query(rs, new ScalarHandler<Integer>(Integer.class));
		System.out.println(id);
		
	}
	
	@Test
	public void query2() {
		
		String sql = "select id from user1 order by id desc";// 默认取第一条
		ResultSet rs = base.getRs(base.getConn(), sql);
		QueryMapping query = QueryMapping.getInstance();
		Integer id = query.query(rs, new ScalarHandler<Integer>(Integer.class));//默认就去第一个字段
		System.out.println(id);
	}
	
	@Test
	public void query3() {
		
		String sql = "select age ,id ,name from user1 where id=1";// 默认取第一条记录的第一个字段（age）
		ResultSet rs = base.getRs(base.getConn(), sql);
		QueryMapping query = QueryMapping.getInstance();
		Integer id = query.query(rs, new ScalarHandler<Integer>(2 ,Integer.class));//如果有多个字段可以自定义取第几个
		System.out.println(id);
	}
	
	//TODO 好困，睡晚在弄根据name取值的
	@Test
	public void query4() {
		
		String sql = "select age, id from user1";//count（*） 是long类型
		ResultSet rs = base.getRs(base.getConn(), sql);
		QueryMapping query = QueryMapping.getInstance();
		Integer id = query.query(rs, new ScalarHandler<Integer>("id" ,Integer.class));//如果有多个字段可以根据字段name取
		System.out.println(id);
	}
	
	@Test
	public void query5() {

		String sql = "select count(*) from user1";
		ResultSet rs = base.getRs(base.getConn(), sql);
		QueryMapping query = QueryMapping.getInstance();
//		Long id = query.query(rs, new ScalarHandler(Long.class));
//		Long id = query.query(rs, new ScalarHandler<Long>(Long.class));
//		Integer id = query.query(rs, new ScalarHandler<Integer>(Integer.class));
//		Integer id = query.query(rs, new ScalarHandler(Integer.class));
		BigDecimal id = query.query(rs, new ScalarHandler<BigDecimal>(BigDecimal.class));
//		BigDecimal id = query.query(rs, new ScalarHandler(BigDecimal.class));
		System.out.println(id);
	}
	
	@Test
	public void query6() {
		String sql = "select t_varchar,"	//java.lang.String
				+ "t_char,"					//java.lang.String
				+ "t_blob,"					//java.lang.byte[]
				+ "t_tinyblob,"				//java.lang.byte[]
				+ "t_mediumblob,"			//java.lang.byte[]
				+ "t_longblob,"				//java.lang.byte[]
				+ "t_text,"					//java.lang.String
				+ "t_tinytext,"				//java.lang.String
				+ "t_mediumtext,"			//java.lang.String
				+ "t_longtext,"				//java.lang.String
				+ "t_integer,"				//java.lang.Integer
				+ "t_tinyint,"				//java.lang.Integer
				+ "t_smallint,"				//java.lang.Integer
				+ "t_mediumint,"			//java.lang.Integer
				+ "t_bit,"					//java.lang.Boolean
				+ "t_bigint,"				//java.lang.Long
				+ "t_float,"				//java.lang.Float
				+ "t_double,"				//java.lang.Double
				+ "t_decimal,"				//java.math.BigDecimal
				+ "t_date,"					//java.sql.Date
				+ "t_time,"					//java.sql.Time
				+ "t_datetime,"				//java.sql.TimeStamp
				+ "t_timestamp,"			//java.sql.TimeStamp
				+ "t_year "					//java.sql.Date
				+ "from user3";
		ResultSet rs = base.getRs(base.getConn(), sql);
		QueryMapping query = QueryMapping.getInstance();
//		String result = query.query(rs, new ScalarHandler<String>("t_varchar"));
//		String result = query.query(rs, new ScalarHandler<String>("t_char"));
//		String result = query.query(rs, new ScalarHandler<String>("t_text"));
//		byte[] result = query.query(rs, new ScalarHandler<byte[]>("t_blob"));
//		String result = query.query(rs, new ScalarHandler<String>("t_tinytext"));
//		String result = query.query(rs, new ScalarHandler<String>("t_mediumtext"));
//		String result = query.query(rs, new ScalarHandler<String>("t_longtext"));
//		Integer result = query.query(rs, new ScalarHandler<Integer>("t_integer"));
//		Integer result = query.query(rs, new ScalarHandler<Integer>("t_tinyint"));
//		Integer result = query.query(rs, new ScalarHandler<Integer>("t_smallint"));
//		Integer result = query.query(rs, new ScalarHandler<Integer>("t_mediumint"));
//		Boolean result = query.query(rs, new ScalarHandler<Boolean>("t_bit"));
//		Long result = query.query(rs, new ScalarHandler<Long>("t_bigint" ,Long.class));
//		Float result = query.query(rs, new ScalarHandler<Float>("t_float"));
//		Double result = query.query(rs, new ScalarHandler<Double>("t_double"));
//		java.math.BigDecimal result = query.query(rs, new ScalarHandler<java.math.BigDecimal>("t_decimal"));
//		java.sql.Date result = query.query(rs, new ScalarHandler<java.sql.Date>("t_date"));
//		java.sql.Time result = query.query(rs, new ScalarHandler<java.sql.Time>("t_time"));
//		java.sql.Timestamp result = query.query(rs, new ScalarHandler<java.sql.Timestamp>("t_datetime"));
//		java.sql.Timestamp result = query.query(rs, new ScalarHandler<java.sql.Timestamp>("t_timestamp"));
//		java.sql.Date result = query.query(rs, new ScalarHandler<java.sql.Date>("t_year"));
		
//		System.out.println(result);
	}
	
	@Test
	public void query7() {
		String sql = "select id from user1 where id=1";
		ResultSet rs = base.getRs(conn, sql);
		QueryMapping query = QueryMapping.getInstance();
		Integer id = query.query(rs, new ScalarHandler<Integer>());
		System.out.println(id);
	}
	
}
