/**  
 * 文件名:    ListBeanTest.java  
 * 描述:      
 * 作者:      suxj
 * 版本:      1.0  
 * 创建时间:  2015年8月9日 下午5:35:29  
 *  
 * 修改历史:  
 * 日期                          作者           版本         描述  
 * ------------------------------------------------------------------  
 * 2015年8月9日        suxj     1.0     1.0 Version  
 */ 
package org.beetl.sql.core.mapping.test.testcase;

import java.sql.ResultSet;
import java.util.List;

import org.beetl.sql.core.UnderlinedNameConversion;
import org.beetl.sql.core.mapping.QueryMapping;
import org.beetl.sql.core.mapping.handler.BeanListHandler;
import org.beetl.sql.core.mapping.test.base.DBBase;
import org.beetl.sql.core.mapping.test.pojo.User1;
import org.beetl.sql.core.mapping.test.pojo.User2;

/**  
 * @ClassName: ListBeanTest   
 * @Description: TODO  
 * @author: suxj  
 * @date:2015年8月9日 下午5:35:29     
 */
@SuppressWarnings("unused")
public class ListBeanTest {

	public static void main(String[] args) {
		DBBase base = DBBase.getInstance();
		
//		HumQuery(base);
		UnderLineQuery(base);
	}
	
//	默认驼峰转换规则
	private static void HumQuery(DBBase base) {
		String sql = "select * from user1";
		ResultSet rs = base.getRs(base.getConn(), sql);
		QueryMapping query = new QueryMapping();
		List<User1> list = query.query(rs, new BeanListHandler<User1>(User1.class));
		
		for(User1 user : list){
			System.out.println(user);
		}
		
//		id=1,name=name1,age=11,sex=M,createDate=2015-08-09,deleteDate=2015-08-09
//		id=2,name=name2,age=12,sex=F,createDate=2015-08-09,deleteDate=2015-08-09
	}
	
//	下划线转换规则
	private static void UnderLineQuery(DBBase base){
//		String sql = "select * from user2";
		String sql = "select id ,t_age from user2";//查询部分字段，其余为java类型默认值
		ResultSet rs = base.getRs(base.getConn(), sql);
		QueryMapping query = new QueryMapping();
		List<User2> list = query.query(rs, new BeanListHandler<User2>(User2.class ,new UnderlinedNameConversion()));
		for(User2 user : list){
			System.out.println(user);
		}
		
//		id=1,name=name1,age=11,sex=M,createDate=2015-08-12,deleteDate=2015-08-09 14:11:48.0
//		id=2,name=name2,age=12,sex=F,createDate=2015-08-22,deleteDate=2015-08-09 14:11:48.0
		
//		id=1,name=null,age=11,sex=null,createDate=null,deleteDate=null
//		id=2,name=null,age=12,sex=null,createDate=null,deleteDate=null
	}
}
