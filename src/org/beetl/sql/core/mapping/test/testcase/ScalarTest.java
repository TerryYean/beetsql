/**  
 * 文件名:    ScalarTest.java  
 * 描述:      
 * 作者:      suxj
 * 版本:      1.0  
 * 创建时间:  2015年8月9日 下午5:44:23  
 *  
 * 修改历史:  
 * 日期                          作者           版本         描述  
 * ------------------------------------------------------------------  
 * 2015年8月9日        suxj     1.0     1.0 Version  
 */ 
package org.beetl.sql.core.mapping.test.testcase;

import java.sql.ResultSet;

import org.beetl.sql.core.UnderlinedNameConversion;
import org.beetl.sql.core.mapping.QueryMapping;
import org.beetl.sql.core.mapping.handler.BeanHandler;
import org.beetl.sql.core.mapping.handler.ScalarHandler;
import org.beetl.sql.core.mapping.test.base.DBBase;
import org.beetl.sql.core.mapping.test.pojo.User1;
import org.beetl.sql.core.mapping.test.pojo.User2;

/**  
 * @ClassName: ScalarTest   
 * @Description: 不需要NameConversion，因为单值处理，只根据index或者columnName获取  
 * @author: suxj  
 * @date:2015年8月9日 下午5:44:23     
 */
@SuppressWarnings("unused")
public class ScalarTest {
	
	public static void main(String[] args) {
		DBBase base = DBBase.getInstance();
		
//		query0(base);
		query1(base);
	}

	private static void query0(DBBase base) {
//		String sql = "select id from user1 where id=1";//1
//		String sql = "select id from user1 order by id desc";//1 默认取第一条
		String sql = "select age ,id ,name from user1 where id=1";//11 默认取第一条记录的第一个字段（age）
//		String sql = "select count(*) from user1";//count（*） 是long类型
		ResultSet rs = base.getRs(base.getConn(), sql);
		QueryMapping query = new QueryMapping();
//		Long id = query.query(rs, new ScalarHandler<Long>());
//		Integer id = query.query(rs, new ScalarHandler<Integer>());//默认就去第一个字段
//		Integer id = query.query(rs, new ScalarHandler<Integer>(1));//如果有多个字段可以自定义取第几个
		Integer id = query.query(rs, new ScalarHandler<Integer>("id"));//如果有多个字段可以根据字段name取
		System.out.println(id);
	}
	
	private static void query1(DBBase base) {
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
				+ "t_bigint,"				//java.lang.Number  bigint要用java.lang.Number来接受
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
		QueryMapping query = new QueryMapping();
//		String result = query.query(rs, new ScalarHandler<String>("t_varchar"));
//		String result = query.query(rs, new ScalarHandler<String>("t_char"));
//		String result = query.query(rs, new ScalarHandler<String>("t_text"));
//		String result = query.query(rs, new ScalarHandler<String>("t_tinytext"));
//		String result = query.query(rs, new ScalarHandler<String>("t_mediumtext"));
//		String result = query.query(rs, new ScalarHandler<String>("t_longtext"));
//		Integer result = query.query(rs, new ScalarHandler<Integer>("t_integer"));
//		Integer result = query.query(rs, new ScalarHandler<Integer>("t_tinyint"));
//		Integer result = query.query(rs, new ScalarHandler<Integer>("t_smallint"));
//		Integer result = query.query(rs, new ScalarHandler<Integer>("t_mediumint"));
//		Boolean result = query.query(rs, new ScalarHandler<Boolean>("t_bit"));
//		Number result = query.query(rs, new ScalarHandler<Number>("t_bigint"));// bigint要用java.lang.Number来接受
//		Float result = query.query(rs, new ScalarHandler<Float>("t_float"));
//		Double result = query.query(rs, new ScalarHandler<Double>("t_double"));
//		java.math.BigDecimal result = query.query(rs, new ScalarHandler<java.math.BigDecimal>("t_decimal"));
//		java.sql.Date result = query.query(rs, new ScalarHandler<java.sql.Date>("t_date"));
//		java.sql.Time result = query.query(rs, new ScalarHandler<java.sql.Time>("t_time"));
//		java.sql.Timestamp result = query.query(rs, new ScalarHandler<java.sql.Timestamp>("t_datetime"));
//		java.sql.Timestamp result = query.query(rs, new ScalarHandler<java.sql.Timestamp>("t_timestamp"));
		java.sql.Date result = query.query(rs, new ScalarHandler<java.sql.Date>("t_year"));
		
		System.out.println(result);
	}
	
}
