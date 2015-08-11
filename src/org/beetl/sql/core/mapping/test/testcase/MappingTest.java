/**  
 * 文件名:    MappingTest.java  
 * 描述:      
 * 作者:      suxj
 * 版本:      1.0  
 * 创建时间:  2015年8月10日 下午4:51:34  
 *  
 * 修改历史:  
 * 日期                          作者           版本         描述  
 * ------------------------------------------------------------------  
 * 2015年8月10日        suxj     1.0     1.0 Version  
 */ 
package org.beetl.sql.core.mapping.test.testcase;

import java.sql.ResultSet;
import java.util.Map;

import org.beetl.sql.core.UnderlinedNameConversion;
import org.beetl.sql.core.mapping.QueryMapping;
import org.beetl.sql.core.mapping.handler.MapHandler;
import org.beetl.sql.core.mapping.test.base.DBBase;

/**  
 * @ClassName: MappingTest   
 * @Description: TODO  
 * @author: suxj  
 * @date:2015年8月10日 下午4:51:34     
 */
public class MappingTest {

	public static void main(String[] args) {
		DBBase base = DBBase.getInstance();
		
		query(base);
	}
	
	private static void query(DBBase base) {
		String sql = "select * from user3";
		ResultSet rs = base.getRs(base.getConn(), sql);
		QueryMapping query = QueryMapping.getInstance();
//		Map<String ,Object> map = query.query(rs, new MapHandler());//断点查看
		Map<String ,Object> map = query.query(rs, new MapHandler(new UnderlinedNameConversion()));//断点查看
		
		//测试忽略key的大小写
		System.out.println(map.get("tvarchar"));
		System.out.println(map.get("TVARCHAR"));
	}
	
}
