package org.beetl.sql.core.mapping.test.testcase;

import java.sql.ResultSet;

import org.beetl.sql.core.UnderlinedNameConversion;
import org.beetl.sql.core.mapping.QueryMapping;
import org.beetl.sql.core.mapping.handler.BeanHandler;
import org.beetl.sql.core.mapping.test.base.DBBase;
import org.beetl.sql.core.mapping.test.pojo.User1;
import org.beetl.sql.core.mapping.test.pojo.User2;

@SuppressWarnings("unused")
public class BeanTest {

	public static void main(String[] args) {
		DBBase base = DBBase.getInstance();
		
//		HumQuery(base);
		UnderLineQuery(base);
	}

//	默认驼峰转换规则
	private static void HumQuery(DBBase base) {
		String sql = "select * from user1 where id=1";
		ResultSet rs = base.getRs(base.getConn(), sql);
		QueryMapping query = new QueryMapping();
		User1 user = query.query(rs, new BeanHandler<User1>(User1.class));
		System.out.println(user);
		
//		id=1,name=name1,age=11,sex=M,createDate=2015-08-09,deleteDate=2015-08-09
	}
	
//	下划线转换规则
	private static void UnderLineQuery(DBBase base){
		String sql = "select * from user2 where id=1";
//		String sql = "select * from user2";//多条记录默认取第一条
//		String sql = "select id ,t_age from user2 where id=1";//查询部分字段，其余为java类型默认值
		ResultSet rs = base.getRs(base.getConn(), sql);
		QueryMapping query = new QueryMapping();
		User2 user = query.query(rs, new BeanHandler<User2>(User2.class ,new UnderlinedNameConversion()));
		System.out.println(user);
		
//		id=1,name=name1,age=11,sex=M,createDate=2015-08-12,deleteDate=2015-08-09 14:11:48.0
//		id=1,name=name1,age=11,sex=M,createDate=2015-08-12,deleteDate=2015-08-09 14:11:48.0
//		id=1,name=null,age=11,sex=null,createDate=null,deleteDate=null
	}
	
}
