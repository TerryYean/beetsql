/**  
 * 文件名:    ListMapTest.java  
 * 描述:      
 * 作者:      suxj
 * 版本:      1.0  
 * 创建时间:  2015年8月10日 下午5:30:06  
 *  
 * 修改历史:  
 * 日期                          作者           版本         描述  
 * ------------------------------------------------------------------  
 * 2015年8月10日        suxj     1.0     1.0 Version  
 */ 
package org.beetl.sql.core.mapping.test.testcase;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.beetl.sql.core.UnderlinedNameConversion;
import org.beetl.sql.core.mapping.QueryMapping;
import org.beetl.sql.core.mapping.handler.MapListHandler;
import org.beetl.sql.core.mapping.test.base.DBBase;

/**  
 * @ClassName: ListMapTest   
 * @Description: TODO  
 * @author: suxj  
 * @date:2015年8月10日 下午5:30:06     
 */
public class ListMapTest {

	public static void main(String[] args) {
		DBBase base = DBBase.getInstance();
		
		query(base);
	}
	
	private static void query(DBBase base) {
//		String sql = "select * from user1";
		String sql = "select * from user2";
		ResultSet rs = base.getRs(base.getConn(), sql);
		QueryMapping query = QueryMapping.getInstance();
//		List<Map<String ,Object>> list = query.query(rs, new MapListHandler());//断点查看
		List<Map<String ,Object>> list = query.query(rs, new MapListHandler(new UnderlinedNameConversion()));//断点查看
		
		//测试忽略key的大小写
		System.out.println(list.get(0).get("tname"));
		System.out.println(list.get(0).get("tNAME"));
	}
	
}
